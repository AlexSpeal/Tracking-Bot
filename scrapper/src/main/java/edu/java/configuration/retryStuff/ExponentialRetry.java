package edu.java.configuration.retryStuff;

import edu.java.configuration.ApplicationConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "retry-config.retry-type", havingValue = "exponential")
public class ExponentialRetry {
    @Bean
    public Retry backoffRetry(ApplicationConfig applicationConfig) {
        return Retry.backoff(applicationConfig.retryConfig().attempts(), applicationConfig.retryConfig().minDelay())
            .filter(throwable -> throwable instanceof WebClientResponseException
                && applicationConfig.retryConfig().statusCodes()
                .contains(((WebClientResponseException) throwable).getStatusCode().value()));
    }
}
