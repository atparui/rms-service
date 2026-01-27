package com.atparui.rmsservice.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;

/**
 * Utility class to load a Spring profile to be used as default.
 */
public final class ProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private ProfileUtil() {}

    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application.
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put(SPRING_PROFILE_DEFAULT, ApplicationConstants.SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }
}
