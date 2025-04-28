package dtu.k30.msc.whm;

import dtu.k30.msc.whm.config.AsyncSyncConfiguration;
import dtu.k30.msc.whm.config.EmbeddedMongo;
import dtu.k30.msc.whm.config.JacksonConfiguration;

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
@SpringBootTest(classes = {WarehousMmgmtApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class})
@EmbeddedMongo
public @interface IntegrationTest {
}
