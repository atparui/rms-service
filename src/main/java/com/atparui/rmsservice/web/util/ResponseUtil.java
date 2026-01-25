package com.atparui.rmsservice.web.util;

import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for ResponseEntity creation.
 */
public final class ResponseUtil {

    private ResponseUtil() {}

    /**
     * Wrap the optional into a ResponseEntity with an OK status, or if it's empty, it returns a ResponseEntity with NOT_FOUND.
     *
     * @param <X>           type of the response
     * @param maybeResponse response to return if present
     * @return response containing {@code maybeResponse} if present or {@link org.springframework.http.HttpStatus#NOT_FOUND}
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return maybeResponse.map(response -> ResponseEntity.ok().body(response)).orElse(ResponseEntity.notFound().build());
    }
}
