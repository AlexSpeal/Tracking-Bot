package edu.java.bot.configuration.retryStuff;

import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "app", name = "retry-config.retry-type", havingValue = "constant")
public class ConstantRetry {
    @Bean
    public Retry backoffRetry(ApplicationConfig applicationConfig) {
        return Retry.fixedDelay(applicationConfig.retryConfig().attempts(), applicationConfig.retryConfig().minDelay())
            .filter(throwable -> throwable instanceof WebClientResponseException
                && applicationConfig.retryConfig().statusCodes()
                .contains(((WebClientResponseException) throwable).getStatusCode().value()))
            .doBeforeRetry(retrySignal -> log.info("retrying... -{}", retrySignal.totalRetries()));
    }
}
