package edu.java.configuration;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient getGitHubClient(@Value("${app.base-url.git-hub-base-url}") String baseUrl) {
        WebClient gitHubWebClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        return new GitHubClient(gitHubWebClient);
    }

    @Bean
    public StackOverflowClient getStackOverflowClient(
        @Value("${app.base-url.stack-overflow-base-url}") String baseUrl
    ) {
        WebClient stackOverflowClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        return new StackOverflowClient(stackOverflowClient);
    }
}
