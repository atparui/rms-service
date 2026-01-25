package com.atparui.rmsservice.web.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for pagination.
 */
public final class PaginationUtil {

    private PaginationUtil() {}

    /**
     * Generate pagination headers for a Spring Data {@link Page} object.
     *
     * @param uriBuilder the URI builder.
     * @param page       the page.
     * @param <T>        the type of object.
     * @return http header.
     */
    public static <T> HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder uriBuilder, Page<T> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        
        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        
        StringBuilder link = new StringBuilder();
        
        if (pageNumber < page.getTotalPages() - 1) {
            link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next"));
        }
        
        if (pageNumber > 0) {
            if (link.length() > 0) {
                link.append(", ");
            }
            link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev"));
        }
        
        if (link.length() > 0) {
            link.append(", ");
        }
        link.append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last"));
        link.append(", ");
        link.append(prepareLink(uriBuilder, 0, pageSize, "first"));
        
        headers.add(HttpHeaders.LINK, link.toString());
        
        return headers;
    }

    private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return "<" + uriBuilder.replaceQueryParam("page", pageNumber).replaceQueryParam("size", pageSize).toUriString() + ">; rel=\"" + relType + "\"";
    }
}
