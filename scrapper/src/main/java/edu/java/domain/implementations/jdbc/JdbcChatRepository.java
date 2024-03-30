package edu.java.domain.implementations.jdbc;

import edu.java.domain.interfaces.ChatRepository;
import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.StateDto;
import edu.java.errors.ChatAlreadyExistsException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(ChatDto chat) {
        try {
            jdbcTemplate.update("INSERT INTO chat(chat_id,created_at,created_by,state) VALUES (?,?,?,?)",
                chat.getChatId(), chat.getCreatedAt(), chat.getCreatedBy(), chat.getState()
            );
        } catch (DataAccessException exception) {
            throw new ChatAlreadyExistsException("Чат уже зарегестрирован!");
        }
    }

    @Transactional
    @Override
    public void remove(Long id) {
        jdbcTemplate.update("DELETE FROM chat WHERE chat_id=?", id);
    }

    @Transactional
    @Override
    public void setState(Long id, String state) {
        jdbcTemplate.update("UPDATE chat SET state=? WHERE chat_id=?",
            state, id
        );
    }

    @Transactional
    @Override
    public String getState(Long id) {
        return jdbcTemplate.query(
                "SELECT state FROM chat WHERE chat_id=?",
                new BeanPropertyRowMapper<>(StateDto.class),
                id
            )
            .getLast().getState();
    }

    @Transactional
    @Override
    public List<ChatDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(ChatDto.class));
    }
}
