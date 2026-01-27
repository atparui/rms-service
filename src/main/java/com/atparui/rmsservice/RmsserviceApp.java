package com.atparui.rmsservice;

import com.atparui.rmsservice.config.ApplicationProperties;
import com.atparui.rmsservice.config.CRLFLogConverter;
import com.atparui.rmsservice.tenant.MultiTenantProperties;
import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.javers.spring.boot.sql.JaversSqlAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import com.atparui.rmsservice.config.ApplicationConstants;
import com.atparui.rmsservice.config.ProfileUtil;

@SpringBootApplication(
    exclude = { JaversSqlAutoConfiguration.class }
)
@EnableConfigurationProperties({ ApplicationProperties.class, MultiTenantProperties.class })
public class RmsserviceApp {

    private static final Logger LOG = LoggerFactory.getLogger(RmsserviceApp.class);

    private final Environment env;

    public RmsserviceApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes rmsservice.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            LOG.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_CLOUD)
        ) {
            LOG.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        SpringApplication app = new SpringApplication(RmsserviceApp.class);
        ProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.warn("The host name could not be determined, using `localhost` as fallback");
        }
        // Get build version information (from environment variables first, then from properties)
        String buildVersion = env.getProperty(
            "BUILD_SHORT_VERSION",
            env.getProperty("app.build.short-version", env.getProperty("jhipster.api-docs.version", "0.0.1"))
        );
        String buildCommitHash = env.getProperty("BUILD_COMMIT_HASH", env.getProperty("app.build.commit-hash", "unknown"));
        String buildCommitCount = env.getProperty("BUILD_COMMIT_COUNT", env.getProperty("app.build.commit-count", "0"));
        String buildBranch = env.getProperty("BUILD_BRANCH", env.getProperty("app.build.branch", "unknown"));
        String buildTimestamp = env.getProperty("BUILD_TIMESTAMP", env.getProperty("app.build.timestamp", "unknown"));

        LOG.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            """

            ----------------------------------------------------------
            \tApplication '{}' is running! Access URLs:
            \tLocal: \t\t{}://localhost:{}{}
            \tExternal: \t{}://{}:{}{}
            \tProfile(s): \t{}
            \tVersion: \t{} (commit: {}, count: {}, branch: {})
            \tBuild Time: \t{}
            ----------------------------------------------------------""",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles(),
            buildVersion,
            buildCommitHash,
            buildCommitCount,
            buildBranch,
            buildTimestamp
        );

        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            // Check if Consul Config is enabled
            String consulConfigEnabled = env.getProperty("spring.cloud.consul.config.enabled");
            String consulHost = env.getProperty("spring.cloud.consul.host", "localhost");
            String consulPort = env.getProperty("spring.cloud.consul.port", "8500");
            if ("true".equalsIgnoreCase(consulConfigEnabled)) {
                configServerStatus = String.format("Consul Config (enabled) - %s:%s", consulHost, consulPort);
            } else {
                configServerStatus = "Not found or not setup for this application";
            }
        }
        LOG.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            "\n----------------------------------------------------------\n\t" +
            "Config Server: \t{}\n----------------------------------------------------------",
            configServerStatus
        );
    }
}
