package edu.java.service;

import edu.java.domain.implementations.jpa.JpaChatRepository;
import edu.java.domain.implementations.jpa.JpaLinkRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.servises.interfaces.LinkService;
import edu.java.servises.interfaces.TgChatService;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.example.dto.response.ListLinksResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JpaLinkServiceTest extends IntegrationTest {
    @Autowired TgChatService tgChatService;
    @Autowired LinkService linkService;
    @Autowired JpaChatRepository jpaChatRepository;
    @Autowired JpaLinkRepository jpaLinkRepository;
    private static final long CHAT_ID = 10L;
    public static final String USERNAME = "AlexSpeal";
    public static final URI LINK;

    static {
        try {
            LINK = new URI("https://github.com/AlexSpeal/Tracking-Bot");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    public static void setType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        tgChatService.register(CHAT_ID, USERNAME);
        linkService.add(CHAT_ID, LINK);
        String link = jpaLinkRepository.findByUrl(LINK.toString()).getUrl();
        assertThat(link).isEqualTo(LINK.toString());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        tgChatService.register(CHAT_ID, USERNAME);
        linkService.add(CHAT_ID, LINK);
        Assertions.assertEquals(1, jpaLinkRepository.findAll().size());
        linkService.remove(CHAT_ID, LINK);
        Assertions.assertTrue(jpaLinkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void getLinkedChadId() {
        tgChatService.register(CHAT_ID, USERNAME);
        linkService.add(CHAT_ID, LINK);
        List<Long> listIds = linkService.getLinkedChadId(jpaLinkRepository.findByUrl(LINK.toString()).getLinkId());
        Assertions.assertEquals(1, listIds.size());
        assertThat(listIds.getFirst()).isEqualTo(CHAT_ID);
    }

    @Test
    @Transactional
    @Rollback
    void listAll() {
        tgChatService.register(CHAT_ID, USERNAME);
        linkService.add(CHAT_ID, LINK);
        ListLinksResponse listLinksResponse = linkService.listAll(CHAT_ID);
        Assertions.assertEquals(1, listLinksResponse.size());
        Assertions.assertEquals(1, listLinksResponse.links().size());
        assertThat(listLinksResponse.links().getFirst().url()).isEqualTo(LINK);

    }
}
