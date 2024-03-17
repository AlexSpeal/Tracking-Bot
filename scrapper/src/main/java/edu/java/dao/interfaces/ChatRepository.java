package edu.java.dao.interfaces;

import edu.java.dto.jdbc.ChatDto;
import java.util.List;

public interface ChatRepository {
    void add(ChatDto object);

    void remove(Long id);

    List<ChatDto> findAll();
}
