package com.atparui.rmsservice.web.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private HeaderUtil() {}

    /**
     * Create a failure alert.
     */
    public static HttpHeaders createFailureAlert(
        String applicationName,
        boolean enableTranslation,
        String entityName,
        String errorKey,
        String defaultMessage
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", defaultMessage);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }

    /**
     * Create entity creation alert.
     */
    public static HttpHeaders createEntityCreationAlert(
        String applicationName,
        boolean enableTranslation,
        String entityName,
        String param
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", "A new " + entityName + " is created with identifier " + param);
        headers.add("X-" + applicationName + "-params", param);
        return headers;
    }

    /**
     * Create entity update alert.
     */
    public static HttpHeaders createEntityUpdateAlert(
        String applicationName,
        boolean enableTranslation,
        String entityName,
        String param
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", "A " + entityName + " is updated with identifier " + param);
        headers.add("X-" + applicationName + "-params", param);
        return headers;
    }

    /**
     * Create entity deletion alert.
     */
    public static HttpHeaders createEntityDeletionAlert(
        String applicationName,
        boolean enableTranslation,
        String entityName,
        String param
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", "A " + entityName + " is deleted with identifier " + param);
        headers.add("X-" + applicationName + "-params", param);
        return headers;
    }
}
