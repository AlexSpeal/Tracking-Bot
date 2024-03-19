package edu.java.dao.interfaces;

import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import java.util.List;

public interface ChatLinkRepository {
    void add(ChatLinkDto object);

    void remove(ChatLinkDto id);

    List<ChatLinkDto> findAll();

    List<ChatLinkDto> getAllTgChatsByLinkId(long linkId);

    List<LinkDto> getAllLinkByChat(long chatId);
}
