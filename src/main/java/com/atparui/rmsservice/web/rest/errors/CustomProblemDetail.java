package com.atparui.rmsservice.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * Custom ProblemDetail with additional properties.
 */
public class CustomProblemDetail extends ProblemDetail {

    private CustomProblemDetail cause;

    public static CustomProblemDetail forStatus(int status) {
        CustomProblemDetail problem = new CustomProblemDetail();
        problem.setStatus(status);
        return problem;
    }

    public static CustomProblemDetail forStatus(HttpStatus status) {
        return forStatus(status.value());
    }

    public CustomProblemDetail withType(URI type) {
        setType(type);
        return this;
    }

    public CustomProblemDetail withTitle(String title) {
        setTitle(title);
        return this;
    }

    public CustomProblemDetail withDetail(String detail) {
        setDetail(detail);
        return this;
    }

    public CustomProblemDetail withProperty(String key, Object value) {
        if (getProperties() == null) {
            setProperties(new HashMap<>());
        }
        getProperties().put(key, value);
        return this;
    }

    public CustomProblemDetail getCause() {
        return cause;
    }

    public void setCause(CustomProblemDetail cause) {
        this.cause = cause;
    }
}
