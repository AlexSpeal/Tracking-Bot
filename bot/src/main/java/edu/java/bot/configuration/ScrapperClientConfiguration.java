package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClientConfiguration {
    @Bean
    public ScrapperClient getScrapperClient(@Value("${app.base-url-scrapper}") String baseUrl) {
        WebClient scrapperClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        return new ScrapperClient(scrapperClient);
    }
}
