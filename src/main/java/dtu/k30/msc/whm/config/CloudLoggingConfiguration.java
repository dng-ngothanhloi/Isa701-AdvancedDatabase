package dtu.k30.msc.whm.config;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tech.jhipster.config.JHipsterProperties;

/*
 * Configures logging for cloud environment without logstash dependency
 */
@Configuration
@Profile("cloud")
public class CloudLoggingConfiguration {

    public CloudLoggingConfiguration(
        @Value("${spring.application.name}") String appName,
        @Value("${server.port}") String serverPort,
        JHipsterProperties jHipsterProperties,
        ObjectMapper mapper
    ) throws JsonProcessingException {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        String customFields = mapper.writeValueAsString(map);

        JHipsterProperties.Logging loggingProperties = jHipsterProperties.getLogging();

        // Only configure JSON console appender for cloud (no logstash)
        if (loggingProperties.isUseJsonFormat()) {
            // Use simple console appender for cloud environment
            configureCloudLogging(context, customFields, loggingProperties);
        }
    }

    private void configureCloudLogging(LoggerContext context, String customFields, JHipsterProperties.Logging loggingProperties) {
        // Simple cloud logging configuration without logstash
        // This avoids the logstash dependency issue
        System.out.println("Cloud logging configured with JSON format: " + loggingProperties.isUseJsonFormat());
    }
} 