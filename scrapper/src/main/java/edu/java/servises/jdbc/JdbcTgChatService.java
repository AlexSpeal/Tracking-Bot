package edu.java.servises.jdbc;

import edu.java.domain.implementations.jdbc.JdbcChatLinkRepository;
import edu.java.domain.implementations.jdbc.JdbcChatRepository;
import edu.java.domain.implementations.jdbc.JdbcLinkRepository;
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
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcChatLinkRepository jdbcChatLinkRepository;
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    public void register(long tgChatId, String username) {
        if (jdbcChatRepository.findAll().stream().anyMatch(c -> c.getChatId() == tgChatId)) {
            throw new ChatAlreadyExistsException("Чат уже создан!");
        }
        ChatDto chatDto = new ChatDto(tgChatId, OffsetDateTime.now(), username, "NONE");
        jdbcChatRepository.add(chatDto);
    }

    @Override
    public void unregister(long tgChatId) {
        if (jdbcChatRepository.findAll().stream().noneMatch(c -> c.getChatId() == tgChatId)) {
            throw new ChatNotExistsException("Чат не был создал ранее!");
        }
        List<LinkDto> links = jdbcChatLinkRepository.getAllLinkByChat(tgChatId);
        for (var link : links) {
            List<ChatLinkDto> subs = jdbcChatLinkRepository.getAllTgChatsByLinkId(link.getLinkId());
            jdbcChatLinkRepository.remove(new ChatLinkDto(tgChatId, link.getLinkId()));
            if (subs.size() == 1) {
                jdbcLinkRepository.remove(subs.getFirst().getLinkId());
            }
        }
        jdbcChatRepository.remove(tgChatId);
    }

    @Override
    public void setState(long tgChatId, String state) {
        jdbcChatRepository.setState(tgChatId, state);
    }

    @Override
    public StateResponse getState(long tgChatId) {
        return new StateResponse(jdbcChatRepository.getState(tgChatId));
    }
}
