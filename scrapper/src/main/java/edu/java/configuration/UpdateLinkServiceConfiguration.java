package edu.java.configuration;

import edu.java.clients.interfaces.UpdateLinkService;
import edu.java.clients.BotClient;
import edu.java.clients.ScrapperQueueProducer;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UpdateLinkServiceConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public UpdateLinkService getBotClient(@Value("${app.base-url-bot}") String baseUrl) {
        WebClient botClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        return new BotClient(botClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public UpdateLinkService getScrapperQueueProducer(
        KafkaTemplate<String, SendUpdateRequest> kafkaTemplate, ApplicationConfig applicationConfig
    ) {
        return new ScrapperQueueProducer(kafkaTemplate, applicationConfig.kafka().topicName());
    }
}
