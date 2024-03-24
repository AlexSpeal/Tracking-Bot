package edu.java.servises.jdbc;

import edu.java.dao.implementations.JdbcChatDao;
import edu.java.dao.implementations.JdbcChatLinkDao;
import edu.java.dao.implementations.JdbcLinkDao;
import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.errors.ChatAlreadyExistsException;
import edu.java.errors.ChatNotExistsException;
import edu.java.servises.interfaces.TgChatService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.StateResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcLinkDao jdbcLinkDao;
    private final JdbcChatLinkDao jdbcChatLinkDao;
    private final JdbcChatDao jdbcChatDao;

    @Override
    public void register(long tgChatId, String username) {
        if (jdbcChatDao.findAll().stream().anyMatch(c -> c.getChatId() == tgChatId)) {
            throw new ChatAlreadyExistsException("Чат уже создан!");
        }
        ChatDto chatDto = new ChatDto(tgChatId, OffsetDateTime.now(), username, "NONE");
        jdbcChatDao.add(chatDto);
    }

    @Override
    public void unregister(long tgChatId) {
        if (jdbcChatDao.findAll().stream().noneMatch(c -> c.getChatId() == tgChatId)) {
            throw new ChatNotExistsException("Чат не был создал ранее!");
        }
        List<LinkDto> links = jdbcChatLinkDao.getAllLinkByChat(tgChatId);
        for (var link : links) {
            List<ChatLinkDto> subs = jdbcChatLinkDao.getAllTgChatsByLinkId(link.getLinkId());
            jdbcChatLinkDao.remove(new ChatLinkDto(tgChatId, link.getLinkId()));
            if (subs.size() == 1) {
                jdbcLinkDao.remove(subs.getFirst().getLinkId());
            }
        }
        jdbcChatDao.remove(tgChatId);
    }

    @Override
    public void setState(long tgChatId, String state) {
        jdbcChatDao.setState(tgChatId, state);
    }

    @Override
    public StateResponse getState(long tgChatId) {
        return new StateResponse(jdbcChatDao.getState(tgChatId));
    }
}
