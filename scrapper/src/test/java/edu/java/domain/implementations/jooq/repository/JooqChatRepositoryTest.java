package edu.java.domain.implementations.jooq.repository;

import edu.java.dto.jdbc.ChatDto;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
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
class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Test
    @Transactional
    @Rollback
    void add() {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        ChatDto chatDto = new ChatDto(12L, data, "Alex", "NONE");
        jooqChatRepository.add(chatDto);
        assertThat(jooqChatRepository.findAll().size()).isEqualTo(1);
        assertThrows(DuplicateKeyException.class, () -> jooqChatRepository.add(chatDto));
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        ChatDto chatDto = new ChatDto(12L, data, "Alex", "NONE");
        jooqChatRepository.add(chatDto);
        jooqChatRepository.remove(12L);
        assertThat(jooqChatRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        ChatDto chatDto1 = new ChatDto(12L, data, "Alex", "NONE");
        data = OffsetDateTime.parse("2021-01-01T10:30:00+00:00");
        ChatDto chatDto2 = new ChatDto(13L, data, "Alexey", "NONE");
        jooqChatRepository.add(chatDto1);
        jooqChatRepository.add(chatDto2);
        List<ChatDto> chatDtoList = List.of(chatDto1, chatDto2);
        assertThat(jooqChatRepository.findAll().size()).isEqualTo(chatDtoList.size());
        assertThat(jooqChatRepository.findAll().getFirst().getChatId()).isEqualTo(chatDtoList.getFirst().getChatId());
    }
}
