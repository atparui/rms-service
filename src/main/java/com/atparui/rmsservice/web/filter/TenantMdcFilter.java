package com.atparui.rmsservice.web.filter;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Populates MDC with tenant ID for all downstream log statements.
 * Reads X-Tenant-ID header (preferred) and also preserves existing Reactor Context TENANT_ID.
 */
@Component
@Order(-90)
public class TenantMdcFilter implements WebFilter {
    private static final String MDC_TENANT_KEY = "tenantId";
    private static final String CTX_TENANT_KEY = "TENANT_ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String headerTenant = exchange.getRequest().getHeaders().getFirst("X-Tenant-ID");

        return chain
            .filter(exchange)
            // ensure tenant is in Reactor context only when header is present
            .contextWrite(ctx -> headerTenant != null && !headerTenant.isBlank() ? ctx.put(CTX_TENANT_KEY, headerTenant) : ctx)
            .doOnSubscribe(s -> setMdc(headerTenant))
            .doOnEach(sig -> {
                if (sig.isOnComplete() || sig.isOnError()) {
                    return;
                }
                String tid = headerTenant;
                if (sig.getContextView().hasKey(CTX_TENANT_KEY)) {
                    tid = sig.getContextView().get(CTX_TENANT_KEY);
                }
                setMdc(tid);
            })
            .doFinally(sig -> MDC.remove(MDC_TENANT_KEY));
    }

    private void setMdc(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) {
            MDC.put(MDC_TENANT_KEY, tenantId);
        } else {
            MDC.remove(MDC_TENANT_KEY);
        }
    }
}
