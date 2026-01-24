package com.atparui.rmsservice.tenant;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Tenant-aware ConnectionFactory that delegates to the appropriate tenant-specific ConnectionFactory
 * based on the current tenant context.
 */
public class TenantAwareConnectionFactory implements ConnectionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TenantAwareConnectionFactory.class);

    private final TenantConnectionFactoryProvider connectionFactoryProvider;
    private final ConnectionFactory defaultConnectionFactory;
    private final ConnectionFactoryMetadata cachedMetadata;

    public TenantAwareConnectionFactory(
        TenantConnectionFactoryProvider connectionFactoryProvider,
        ConnectionFactory defaultConnectionFactory
    ) {
        this.connectionFactoryProvider = connectionFactoryProvider;
        this.defaultConnectionFactory = defaultConnectionFactory;
        // Cache metadata at construction time to avoid infinite recursion
        // when Spring AOP proxies are involved
        this.cachedMetadata = getMetadataSafely(defaultConnectionFactory);
    }

    /**
     * Safely extracts metadata from a ConnectionFactory, avoiding circular calls.
     * This method handles the case where the factory might be a TenantAwareConnectionFactory
     * (which shouldn't happen with proper configuration, but we handle it defensively).
     */
    private ConnectionFactoryMetadata getMetadataSafely(ConnectionFactory factory) {
        try {
            // If the factory is a TenantAwareConnectionFactory,
            // we need to get the underlying factory's metadata to avoid recursion
            if (factory instanceof TenantAwareConnectionFactory) {
                TenantAwareConnectionFactory tenantAware = (TenantAwareConnectionFactory) factory;
                LOG.warn("Detected TenantAwareConnectionFactory in defaultConnectionFactory - this indicates a configuration issue");
                return getMetadataSafely(tenantAware.defaultConnectionFactory);
            }

            // For Spring AOP proxies, try to check if the target is a TenantAwareConnectionFactory
            if (org.springframework.aop.support.AopUtils.isAopProxy(factory)) {
                Class<?> targetClass = org.springframework.aop.support.AopUtils.getTargetClass(factory);
                if (TenantAwareConnectionFactory.class.isAssignableFrom(targetClass)) {
                    LOG.warn("Detected proxied TenantAwareConnectionFactory - this indicates a configuration issue");
                    // We can't safely unwrap and call, so we'll throw an exception
                    throw new IllegalStateException(
                        "Circular reference detected: TenantAwareConnectionFactory is referencing itself. " +
                        "Check TenantAwareDatabaseConfiguration to ensure it's getting the correct default ConnectionFactory."
                    );
                }
            }

            // Call getMetadata() on the factory
            return factory.getMetadata();
        } catch (StackOverflowError e) {
            LOG.error("StackOverflowError detected while getting metadata - possible circular reference");
            throw new IllegalStateException(
                "Circular reference detected in ConnectionFactory metadata resolution. " +
                "This usually indicates a misconfiguration where TenantAwareConnectionFactory " +
                "is referencing itself.",
                e
            );
        } catch (Exception e) {
            LOG.warn("Error getting metadata from connection factory, using fallback: {}", e.getMessage());
            // Return a minimal metadata to avoid breaking the application
            return new ConnectionFactoryMetadata() {
                @Override
                public String getName() {
                    return "Unknown";
                }
            };
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Publisher<? extends Connection> create() {
        Mono<Connection> connectionMono = Mono.from(connectionFactoryProvider.getConnectionFactory())
            .switchIfEmpty(Mono.just(defaultConnectionFactory))
            .flatMap(factory -> {
                LOG.debug(
                    "[TENANT-FACTORY] Using ConnectionFactory: {} (class: {})",
                    factory,
                    factory.getClass().getName()
                );
                Publisher<? extends Connection> publisher = factory.create();
                return (Mono<Connection>) Mono
                    .from(publisher)
                    .doOnNext(connection -> LOG.debug(
                        "[TENANT-FACTORY] Obtained connection {} (class: {}) via factory {} (class: {})",
                        connection,
                        connection.getClass().getName(),
                        factory,
                        factory.getClass().getName()
                    ));
            })
            .onErrorResume(error -> {
                LOG.error("Error creating tenant-specific connection, falling back to default: {}", error.getMessage());
                Publisher<? extends Connection> fallbackPublisher = defaultConnectionFactory.create();
                return (Mono<Connection>) Mono.from(fallbackPublisher);
            });
        return connectionMono;
    }

    @Override
    public ConnectionFactoryMetadata getMetadata() {
        // Return cached metadata to avoid infinite recursion
        return cachedMetadata;
    }
}
