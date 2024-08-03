package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    String baseUrlScrapper,
    @NotEmpty
    String telegramToken,
    RetryConfig retryConfig,
    @NotNull
    boolean useQueue,

    @NotNull
    KafkaConfig kafka) {
    public record RetryConfig(RetryType retryType, int attempts, Duration minDelay, List<Integer> statusCodes) {
        public enum RetryType {
            CONSTANT, LINEAR, EXPONENTIAL
        }

    }

    public record KafkaConfig(@NotNull String bootstrapServer,
                              @NotNull String topicName,
                              @NotNull int partitionsCount,
                              @NotNull short replicationCount) {
    }
}
