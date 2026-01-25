package com.atparui.rmsservice;

import com.atparui.rmsservice.config.AsyncSyncConfiguration;
import com.atparui.rmsservice.config.EmbeddedKafka;
import com.atparui.rmsservice.config.EmbeddedSQL;
import com.atparui.rmsservice.config.JacksonConfiguration;
import com.atparui.rmsservice.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { RmsserviceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
    String DEFAULT_TIMEOUT = "PT5S";

    String DEFAULT_ENTITY_TIMEOUT = "PT5S";
}
