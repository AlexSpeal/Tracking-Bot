package edu.java.servises.interfaces;

import org.example.dto.response.StateResponse;

public interface TgChatService {
    void register(long tgChatId, String username);

    void unregister(long tgChatId);

    void setState(long tgChatId, String state);

    Boolean isRegister(long tgChatId);

    StateResponse getState(long tgChatId);
}
