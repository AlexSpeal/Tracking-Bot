package edu.java.domain.implementations.jdbc;

import edu.java.domain.interfaces.ChatRepository;
import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.StateDto;
import java.util.List;
import lombok.AllArgsConstructor;
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
        jdbcTemplate.update("INSERT INTO chat(chat_id,created_at,created_by,state) VALUES (?,?,?,?)",
            chat.getChatId(), chat.getCreatedAt(), chat.getCreatedBy(), chat.getState()
        );
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

    @Override
    public Boolean isRegister(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS(SELECT chat_id FROM chat WHERE chat_id = ?)", Boolean.class, id
        );
    }

    @Transactional
    @Override
    public List<ChatDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(ChatDto.class));
    }
}
