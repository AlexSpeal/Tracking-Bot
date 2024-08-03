package edu.java.clients;

import edu.java.clients.interfaces.UpdateLinkService;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.SendUpdateRequest;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class ScrapperQueueProducer implements UpdateLinkService {
    private final String topicName;
    private final KafkaTemplate<String, SendUpdateRequest> kafkaTemplate;

    public ScrapperQueueProducer(KafkaTemplate<String, SendUpdateRequest> kafkaTemplate, String topicName) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updates(SendUpdateRequest request) {
        kafkaTemplate.send(topicName, request).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("massage was sent offset:{}", result.getRecordMetadata());
            } else {
                log.info("send error");
            }
        });
    }
}
