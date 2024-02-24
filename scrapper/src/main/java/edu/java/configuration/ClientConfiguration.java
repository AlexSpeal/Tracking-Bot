package edu.java.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ClientConfiguration {
    @Autowired
    ApplicationConfig applicationConfig;

    @Bean("getGitHubClient")
    public WebClient getGitHubClient(@Value("${app.git-hub-base-url}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }

    @Bean("getStackOverflowClient")
    public WebClient getStackOverflowClient(
        @Value("${app.stack-overflow-base-url}") String baseUrl
    ) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}


