package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "scrapper-client", ignoreUnknownFields = false)
public record ScrapperConfig(
    @NotEmpty
    String baseUrl
) {
}
