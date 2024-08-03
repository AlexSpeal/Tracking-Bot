package edu.java.servises.jooq;

import edu.java.domain.implementations.jooq.repository.JooqChatLinkRepository;
import edu.java.domain.implementations.jooq.repository.JooqChatRepository;
import edu.java.domain.implementations.jooq.repository.JooqLinkRepository;
import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.servises.interfaces.TgChatService;
import errors.ChatAlreadyExistsException;
import errors.ChatNotExistsException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.StateResponse;

@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    private final JooqChatRepository jooqChatRepository;
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqChatLinkRepository jooqChatLinkRepository;

    @Override
    public void register(long tgChatId, String username) {
        if (jooqChatRepository.isRegister(tgChatId)) {
            throw new ChatAlreadyExistsException("Чат уже создан!");
        }
        ChatDto chatDto = new ChatDto(tgChatId, OffsetDateTime.now(), username, "NONE");
        jooqChatRepository.add(chatDto);
    }

    @Override
    public void unregister(long tgChatId) {
        if (!jooqChatRepository.isRegister(tgChatId)) {
            throw new ChatNotExistsException("Чат не был создал ранее!");
        }
        List<LinkDto> links = jooqChatLinkRepository.getAllLinkByChat(tgChatId);
        for (var link : links) {
            List<ChatLinkDto> subs = jooqChatLinkRepository.getAllTgChatsByLinkId(link.getLinkId());
            jooqChatLinkRepository.remove(new ChatLinkDto(tgChatId, link.getLinkId()));
            if (subs.size() == 1) {
                jooqLinkRepository.remove(subs.getFirst().getLinkId());
            }
        }
        jooqChatRepository.remove(tgChatId);
    }

    @Override
    public void setState(long tgChatId, String state) {
        jooqChatRepository.setState(tgChatId, state);
    }

    @Override
    public Boolean isRegister(long tgChatId) {
        return jooqChatRepository.isRegister(tgChatId);
    }

    @Override
    public StateResponse getState(long tgChatId) {
        return new StateResponse(jooqChatRepository.getState(tgChatId));
    }
}
