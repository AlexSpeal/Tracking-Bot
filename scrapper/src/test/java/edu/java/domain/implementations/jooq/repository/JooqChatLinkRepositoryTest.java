package edu.java.domain.implementations.jooq.repository;

import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

@DirtiesContext
class JooqChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatLinkRepository jooqChatLinkRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    private static final long CHAT_ID = 14L;
    private static final OffsetDateTime data1 =
        OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime data2 =
        OffsetDateTime.of(2023, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC);
    public static final ChatDto chatDto = new ChatDto(
        CHAT_ID,
        OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC),
        "Alex", "NONE"
    );
    private static final LinkDto linkDto =
        new LinkDto(
            URI.create("https://github.com/onevoker/LinkTrackerBot"), data1, data1
            , "github", "{бурури,парирам}"
        );
    private static final LinkDto linkDto2 =
        new LinkDto(
            URI.create("https://github.com/onevoker/LinkTracker"),
            data2, data2, "github", "{мими,мамому}"
        );

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jooqChatRepository.add(chatDto);
        jooqLinkRepository.add(linkDto);

        ChatLinkDto chatLink = new ChatLinkDto(CHAT_ID, jooqLinkRepository.findAll().getFirst().getLinkId());
        jooqChatLinkRepository.add(chatLink);

        assertThat(jooqChatLinkRepository.findAll().size()).isEqualTo(1);
        assertThrows(DuplicateKeyException.class, () -> jooqChatLinkRepository.add(chatLink));
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jooqChatRepository.add(chatDto);
        jooqLinkRepository.add(linkDto);
        ChatLinkDto chatLink = new ChatLinkDto(CHAT_ID, jooqLinkRepository.findAll().getFirst().getLinkId());
        jooqChatLinkRepository.add(chatLink);
        assertThat(jooqChatLinkRepository.findAll().size()).isEqualTo(1);
        jooqChatLinkRepository.remove(chatLink);
        assertThat(jooqChatLinkRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {

        jooqLinkRepository.add(linkDto);
        jooqChatRepository.add(chatDto);
        jooqLinkRepository.add(linkDto2);
        jooqChatLinkRepository.add(new ChatLinkDto(
            chatDto.getChatId(),
            jooqLinkRepository.findAll().getFirst().getLinkId()
        ));
        jooqChatLinkRepository.add(new ChatLinkDto(
            chatDto.getChatId(),
            jooqLinkRepository.findAll().getLast().getLinkId()
        ));
        List<ChatLinkDto> chatLinkDtoList =
            List.of(new ChatLinkDto(chatDto.getChatId(), jooqLinkRepository.findAll().getFirst()
                .getLinkId()), new ChatLinkDto(chatDto.getChatId(), jooqLinkRepository.findAll().getLast()
                .getLinkId()));
        assertThat(jooqChatLinkRepository.findAll()).isEqualTo(chatLinkDtoList);
    }
}
