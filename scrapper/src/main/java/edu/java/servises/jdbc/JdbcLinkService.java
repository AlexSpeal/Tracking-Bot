package edu.java.servises.jdbc;

import edu.java.domain.implementations.jdbc.JdbcChatLinkRepository;
import edu.java.domain.interfaces.LinkRepository;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.dto.jdbc.github.Github;
import edu.java.dto.jdbc.stackOverflow.Question;
import edu.java.errors.LinkWasNotTrackedException;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.LinkResponse;
import org.example.dto.response.ListLinksResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository jdbcLinkRepository;
    private final JdbcChatLinkRepository jdbcChatLinkRepository;
    private final GithubHandler githubHandler;
    private final StackOverflowHandler stackOverflowHandler;
    private final static String GITHUB = "github";
    private final static String STACKOVERFLOW = "stackoverflow";

    @Override
    public void add(long tgChatId, URI url) {
        List<LinkDto> linkDtoList = jdbcLinkRepository.getByUri(url);
        if (linkDtoList.isEmpty()) {
            addLinkIfNotExist(tgChatId, url);
        } else {
            jdbcChatLinkRepository.add(new ChatLinkDto(tgChatId, linkDtoList.getFirst().getLinkId()));
        }
    }

    @Override
    public void remove(long tgChatId, URI url) {
        List<LinkDto> linkDtoList = jdbcLinkRepository.getByUri(url);
        if (linkDtoList.isEmpty()) {
            throw new LinkWasNotTrackedException("Ссылка не отслеживается!");
        }
        ChatLinkDto chatLinkDto = new ChatLinkDto(tgChatId, linkDtoList.getFirst().getLinkId());

        jdbcChatLinkRepository.remove(chatLinkDto);
        List<ChatLinkDto> linkDtos = jdbcChatLinkRepository.getAllTgChatsByLinkId(linkDtoList.getFirst().getLinkId());
        if (linkDtos.isEmpty()) {
            jdbcLinkRepository.remove(linkDtoList.getFirst().getLinkId());
        }
    }

    @Override
    public List<Long> getLinkedChadId(long linkId) {
        return jdbcChatLinkRepository.getAllTgChatsByLinkId(linkId).stream().map(ChatLinkDto::getChatId).toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<LinkDto> chatLinkDtoList = jdbcChatLinkRepository.getAllLinkByChat(tgChatId);
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
        jdbcLinkRepository.add(linkDto);

        ChatLinkDto chatLinkDto = new ChatLinkDto(tgChatId, jdbcLinkRepository.getByUri(url).getFirst().getLinkId());
        jdbcChatLinkRepository.add(chatLinkDto);
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
