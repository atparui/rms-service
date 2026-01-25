package com.atparui.rmsservice.tenant;

import reactor.util.context.Context;

/**
 * Utility class for managing tenant context in reactive streams.
 * Uses Reactor Context to propagate tenant ID through the reactive chain.
 */
public final class TenantContextHolder {

    private static final String TENANT_ID_KEY = "TENANT_ID";
    private static final ThreadLocal<String> TENANT_THREAD_LOCAL = new ThreadLocal<>();

    private TenantContextHolder() {}

    /**
     * Get the current tenant ID from Reactor Context or ThreadLocal.
     *
     * @return Mono containing the tenant ID, or empty if not set
     */
    public static reactor.core.publisher.Mono<String> getCurrentTenantId() {
        return reactor.core.publisher.Mono.deferContextual(ctx -> {
            String tenantId = ctx.getOrDefault(TENANT_ID_KEY, null);
            if (tenantId != null) {
                return reactor.core.publisher.Mono.just(tenantId);
            }
            String threadTenant = TENANT_THREAD_LOCAL.get();
            return threadTenant != null ? reactor.core.publisher.Mono.just(threadTenant) : reactor.core.publisher.Mono.empty();
        });
    }

    /**
     * Set the tenant ID in Reactor Context and ThreadLocal.
     *
     * @param tenantId the tenant ID to set
     * @return Context with tenant ID added
     */
    public static Context setTenantId(String tenantId) {
        TENANT_THREAD_LOCAL.set(tenantId);
        return Context.of(TENANT_ID_KEY, tenantId);
    }

    /**
     * Set tenant ID in ThreadLocal only (for servlet/imperative flows).
     */
    public static void setTenantIdThreadLocal(String tenantId) {
        TENANT_THREAD_LOCAL.set(tenantId);
    }

    /**
     * Clear tenant ThreadLocal (call at request end in servlet stack).
     */
    public static void clearTenant() {
        TENANT_THREAD_LOCAL.remove();
    }

    /**
     * Get tenant ID from Context directly (for non-reactive code).
     *
     * @param context the Reactor Context
     * @return the tenant ID or null if not present
     */
    public static String getTenantIdFromContext(Context context) {
        return context.getOrDefault(TENANT_ID_KEY, null);
    }
}
