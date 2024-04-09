package edu.java.bot.service;

import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BotQueueConsumer {
    private final BotService botService;
    @Autowired
    private final ApplicationConfig applicationConfig;

    public BotQueueConsumer(BotService botService, ApplicationConfig applicationConfig) {
        this.botService = botService;
        this.applicationConfig = applicationConfig;
    }

    @RetryableTopic(attempts = "5", dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    @KafkaListener(topics = "${app.kafka.topic-name}",
                   groupId = "group1",
                   containerFactory = "kafkaListenerContainerFactory")
    public void sendUpdates(SendUpdateRequest sendUpdateRequest) {
        botService.sendUpdate(sendUpdateRequest);
        log.info("message received");
    }

    @DltHandler
    public void sendUpdatesDlt(SendUpdateRequest sendUpdateRequest) {
        log.info("message received to dlt");
    }
}
