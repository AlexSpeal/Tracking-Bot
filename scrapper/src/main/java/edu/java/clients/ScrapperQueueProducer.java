package edu.java.clients;

import edu.java.clients.interfaces.UpdateLinkService;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.kafka.core.KafkaTemplate;

public class ScrapperQueueProducer implements UpdateLinkService {
    private final String topicName;
    private final KafkaTemplate<String, SendUpdateRequest> kafkaTemplate;

    public ScrapperQueueProducer(KafkaTemplate<String, SendUpdateRequest> kafkaTemplate, String topicName) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updates(SendUpdateRequest request) {
        kafkaTemplate.send(topicName, request);
    }
}
