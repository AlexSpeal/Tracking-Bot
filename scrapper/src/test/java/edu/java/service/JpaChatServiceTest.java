package edu.java.service;

import edu.java.domain.implementations.jpa.JpaChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.servises.interfaces.TgChatService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JpaChatServiceTest extends IntegrationTest {
    @Autowired TgChatService tgChatService;
    @Autowired JpaChatRepository jpaChatRepository;
    private static final long CHAT_ID = 10L;
    public static final String ADD = "ADD";
    public static final String NONE = "NONE";

    public static final String USERNAME = "AlexSpeal";

    @DynamicPropertySource
    public static void setType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    void register() {
        tgChatService.register(CHAT_ID, USERNAME);
        var chatId = jpaChatRepository.findById(CHAT_ID).orElseThrow().getChatId();
        assertThat(chatId).isEqualTo(CHAT_ID);
    }

    @Test
    @Transactional
    @Rollback
    void unregister() {
        tgChatService.register(CHAT_ID, USERNAME);
        Assertions.assertEquals(1, jpaChatRepository.findAll().size());
        tgChatService.unregister(CHAT_ID);
        Assertions.assertTrue(jpaChatRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void setState() {
        tgChatService.register(CHAT_ID, USERNAME);
        tgChatService.setState(CHAT_ID, ADD);
        Assertions.assertEquals(ADD, jpaChatRepository.findById(CHAT_ID).orElseThrow().getState());

    }

    @Test
    @Transactional
    @Rollback
    void getState() {
        tgChatService.register(CHAT_ID, USERNAME);
        Assertions.assertEquals(NONE, jpaChatRepository.findById(CHAT_ID).orElseThrow().getState());
    }
}
