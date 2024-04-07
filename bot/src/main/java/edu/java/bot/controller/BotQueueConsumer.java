package edu.java.bot.controller;

import edu.java.bot.configuration.ApplicationConfig;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
public class BotQueueConsumer {
    private final BotService botService;
    @Autowired
    private final ApplicationConfig applicationConfig;

    public BotQueueConsumer(BotService botService, ApplicationConfig applicationConfig) {
        this.botService = botService;
        this.applicationConfig = applicationConfig;
    }

    @KafkaListener(topics = "${app.kafka.topic-name}")
    public void sendUpdates(SendUpdateRequest sendUpdateRequest) {
        botService.sendUpdate(sendUpdateRequest);
    }
}
