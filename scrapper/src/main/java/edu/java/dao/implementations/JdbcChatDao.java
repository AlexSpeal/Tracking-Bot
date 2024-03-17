package edu.java.dao.implementations;

import edu.java.dao.interfaces.ChatRepository;
import edu.java.dto.jdbc.ChatDto;
import edu.java.errors.ChatAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@AllArgsConstructor
public class JdbcChatDao implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;
    @Transactional
    @Override
    public void add(ChatDto chat) {
        try {
            jdbcTemplate.update("INSERT INTO chat(chat_id,created_at,created_by) VALUES (?,?,?)",
                chat.getChat_id(), chat.getCreatedAt(), chat.getCreatedBy()
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
    public List<ChatDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(ChatDto.class));
    }
}
