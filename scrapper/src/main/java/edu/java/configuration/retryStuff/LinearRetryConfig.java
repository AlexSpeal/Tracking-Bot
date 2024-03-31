package edu.java.configuration.retryStuff;

import edu.java.configuration.ApplicationConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "retry-config.retry-type", havingValue = "linear")
public class LinearRetryConfig {
    @Bean
    public Retry backoffRetry(ApplicationConfig applicationConfig) {
        return new LinearRetry(
            applicationConfig.retryConfig().attempts(),
            applicationConfig.retryConfig().minDelay(),
            applicationConfig.retryConfig().statusCodes()
        );
    }
}
