package edu.java.dao.implementations;

import edu.java.dto.jdbc.ChatDto;
import edu.java.errors.ChatAlreadyExistsException;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

class JdbcChatDaoTest extends IntegrationTest {
    @Autowired
    private JdbcChatDao jdbcChatDao;

    @Test
    @Transactional
    @Rollback
    void add() {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        ChatDto chatDto = new ChatDto(12L, data, "Alex","NONE");
        jdbcChatDao.add(chatDto);
        assertThat(jdbcChatDao.findAll().size()).isEqualTo(1);
        var exception = assertThrows(ChatAlreadyExistsException.class, () -> jdbcChatDao.add(chatDto));
        assertThat(exception.getMessage()).isEqualTo("Чат уже зарегестрирован!");
    }
    @Test
    @Transactional
    @Rollback
    void remove() {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        ChatDto chatDto = new ChatDto(12L, data, "Alex","NONE");
        jdbcChatDao.add(chatDto);
        jdbcChatDao.remove(12L);
        assertThat(jdbcChatDao.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        OffsetDateTime data = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
        ChatDto chatDto1 = new ChatDto(12L, data, "Alex","NONE");
        data = OffsetDateTime.parse("2021-01-01T10:30:00+00:00");
        ChatDto chatDto2 = new ChatDto(13L, data, "Alexey","NONE");
        jdbcChatDao.add(chatDto1);
        jdbcChatDao.add(chatDto2);
        List<ChatDto> chatDtoList=List.of(chatDto1,chatDto2);
        assertThat(jdbcChatDao.findAll()).isEqualTo(chatDtoList);
    }
}
