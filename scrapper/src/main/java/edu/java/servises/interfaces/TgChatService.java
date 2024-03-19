package edu.java.servises.interfaces;

public interface TgChatService {
    void register(long tgChatId, String username);

    void unregister(long tgChatId);
}
