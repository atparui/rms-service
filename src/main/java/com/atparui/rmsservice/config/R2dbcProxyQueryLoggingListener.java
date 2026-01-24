package com.atparui.rmsservice.config;

import io.r2dbc.proxy.core.Binding;
import io.r2dbc.proxy.core.Bindings;
import io.r2dbc.proxy.core.BoundValue;
import io.r2dbc.proxy.core.QueryExecutionInfo;
import io.r2dbc.proxy.core.QueryInfo;
import io.r2dbc.proxy.listener.ProxyExecutionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs R2DBC queries with bindings and timings so we can see the exact SQL executed.
 */
public class R2dbcProxyQueryLoggingListener implements ProxyExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger("io.r2dbc.proxy.query");

    @Override
    public void beforeQuery(QueryExecutionInfo executionInfo) {
        LOG.debug(
            "R2DBC before -> type: {}, batchSize: {}, queries: {}, bindings: {}",
            executionInfo.getType(),
            executionInfo.getBatchSize(),
            renderQueries(executionInfo),
            renderBindings(executionInfo)
        );
    }

    @Override
    public void afterQuery(QueryExecutionInfo executionInfo) {
        LOG.debug(
            "R2DBC after -> success: {}, durationMs: {}, queries: {}, bindings: {}",
            executionInfo.isSuccess(),
            executionInfo.getExecuteDuration().toMillis(),
            renderQueries(executionInfo),
            renderBindings(executionInfo)
        );
    }

    private String renderQueries(QueryExecutionInfo executionInfo) {
        return executionInfo.getQueries().stream().map(QueryInfo::getQuery).collect(Collectors.joining(" | "));
    }

    private String renderBindings(QueryExecutionInfo executionInfo) {
        List<String> bindingSummaries = new ArrayList<>();
        for (QueryInfo queryInfo : executionInfo.getQueries()) {
            List<Bindings> bindingsList = queryInfo.getBindingsList();
            if (bindingsList == null || bindingsList.isEmpty()) {
                bindingSummaries.add("[]");
                continue;
            }
            String rendered = bindingsList.stream().map(this::renderSingleBinding).collect(Collectors.joining(" , ", "[", "]"));
            bindingSummaries.add(rendered);
        }
        return String.join(" | ", bindingSummaries);
    }

    private String renderSingleBinding(Bindings bindings) {
        List<String> parts = new ArrayList<>();
        appendBindings(parts, bindings.getIndexBindings());
        appendBindings(parts, bindings.getNamedBindings());
        return String.join(", ", parts);
    }

    private void appendBindings(List<String> parts, Set<Binding> bindings) {
        if (bindings == null || bindings.isEmpty()) {
            return;
        }
        for (Binding binding : bindings) {
            parts.add(binding.getKey() + "=" + renderValue(binding.getBoundValue()));
        }
    }

    private String renderValue(BoundValue value) {
        if (value == null) {
            return "null";
        }
        if (value.isNull()) {
            return "null<" + (value.getNullType() != null ? value.getNullType().getSimpleName() : "?") + ">";
        }
        Object raw = value.getValue();
        return raw == null ? "null" : raw + "<" + raw.getClass().getSimpleName() + ">";
    }
}
