package edu.java.domain.interfaces;

import edu.java.dto.jdbc.ChatDto;
import java.util.List;

public interface ChatRepository {
    void add(ChatDto object);

    void remove(Long id);

    void setState(Long id, String state);

    String getState(Long id);

    Boolean isRegister(Long id);

    List<ChatDto> findAll();
}
