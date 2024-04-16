package edu.java.servises.jooq;

import edu.java.domain.implementations.jooq.repository.JooqChatLinkRepository;
import edu.java.domain.implementations.jooq.repository.JooqLinkRepository;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.dto.jdbc.github.Github;
import edu.java.dto.jdbc.stackOverflow.Question;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import errors.LinkWasNotTrackedException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.LinkResponse;
import org.example.dto.response.ListLinksResponse;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqChatLinkRepository jooqChatLinkRepository;
    private final GithubHandler githubHandler;
    private final StackOverflowHandler stackOverflowHandler;
    private final static String GITHUB = "github";
    private final static String STACKOVERFLOW = "stackoverflow";

    @Override
    public void add(long tgChatId, URI url) {
        List<LinkDto> linkDtoList = jooqLinkRepository.getByUri(url);
        if (linkDtoList.isEmpty()) {
            addLinkIfNotExist(tgChatId, url);
        } else {
            jooqChatLinkRepository.add(new ChatLinkDto(tgChatId, linkDtoList.getFirst().getLinkId()));
        }
    }

    @Override
    public void remove(long tgChatId, URI url) {
        List<LinkDto> linkDtoList = jooqLinkRepository.getByUri(url);
        if (linkDtoList.isEmpty()) {
            throw new LinkWasNotTrackedException("Ссылка не отслеживается!");
        }
        ChatLinkDto chatLinkDto = new ChatLinkDto(tgChatId, linkDtoList.getFirst().getLinkId());

        jooqChatLinkRepository.remove(chatLinkDto);
        List<ChatLinkDto> linkDtos = jooqChatLinkRepository.getAllTgChatsByLinkId(linkDtoList.getFirst().getLinkId());
        if (linkDtos.isEmpty()) {
            jooqLinkRepository.remove(linkDtoList.getFirst().getLinkId());
        }
    }

    @Override
    public List<Long> getLinkedChadId(long linkId) {
        return jooqChatLinkRepository.getAllTgChatsByLinkId(linkId).stream().map(ChatLinkDto::getChatId).toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<LinkDto> chatLinkDtoList = jooqChatLinkRepository.getAllLinkByChat(tgChatId);
        List<LinkResponse> linkResponses = new ArrayList<>();
        for (var link : chatLinkDtoList) {
            linkResponses.add(new LinkResponse(link.getLinkId(), link.getUrl()));
        }
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    void addLinkIfNotExist(long tgChatId, URI url) {
        OffsetDateTime data = OffsetDateTime.now();
        String type = getType(url);
        String dataRep = getDate(type, url);
        LinkDto linkDto = new LinkDto(url, data, data, type, dataRep);
        jooqLinkRepository.add(linkDto);

        ChatLinkDto chatLinkDto = new ChatLinkDto(tgChatId, jooqLinkRepository.getByUri(url).getFirst().getLinkId());
        jooqChatLinkRepository.add(chatLinkDto);
    }

    String getType(URI uri) {
        if (uri.getHost().contains("github.com")) {
            return GITHUB;
        } else if (uri.getHost().contains("stackoverflow.com")) {
            return STACKOVERFLOW;
        } else {
            return "";
        }

    }

    String getDate(String type, URI url) {
        switch (type) {
            case GITHUB:
                Github github = githubHandler.getInfoByUrl(url);
                return githubHandler.getData(github);
            case STACKOVERFLOW:
                Question sof = stackOverflowHandler.getInfoByUrl(url);
                return stackOverflowHandler.getData(sof);
            default:
                return "";
        }
    }
}
