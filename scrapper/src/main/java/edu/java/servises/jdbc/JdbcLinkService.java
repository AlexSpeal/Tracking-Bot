package edu.java.servises.jdbc;

import edu.java.dao.implementations.JdbcChatLinkDao;
import edu.java.dao.implementations.JdbcLinkDao;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import edu.java.errors.LinkWasNotTrackedException;
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
    private final JdbcLinkDao jdbcLinkDao;
    private final JdbcChatLinkDao jdbcChatLinkDao;

    @Override
    public void add(long tgChatId, URI url) {
        List<LinkDto> linkDtoList = jdbcLinkDao.getByUri(url);
        if (linkDtoList.isEmpty()) {
            addLinkIfNotExist(tgChatId, url);
        } else {
            jdbcChatLinkDao.add(new ChatLinkDto(tgChatId, linkDtoList.getFirst().getLinkId()));
        }
    }

    @Override
    public void remove(long tgChatId, URI url) {
        List<LinkDto> linkDtoList = jdbcLinkDao.getByUri(url);
        if (linkDtoList.isEmpty()) {
            throw new LinkWasNotTrackedException("Ссылка не отслеживается!");
        }
        ChatLinkDto chatLinkDto = new ChatLinkDto(tgChatId, linkDtoList.getFirst().getLinkId());

        jdbcChatLinkDao.remove(chatLinkDto);
        List<ChatLinkDto> linkDtos = jdbcChatLinkDao.getAllTgChatsByLinkId(linkDtoList.getFirst().getLinkId());
        if (linkDtos.isEmpty()) {
            jdbcLinkDao.remove(linkDtoList.getFirst().getLinkId());
        }
    }

    @Override
    public List<Long> getLinkedChadId(long linkId) {
        return jdbcChatLinkDao.getAllTgChatsByLinkId(linkId).stream().map(ChatLinkDto::getLinkId).toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<LinkDto> chatLinkDtoList = jdbcChatLinkDao.getAllLinkByChat(tgChatId);
        List<LinkResponse> linkResponses = new ArrayList<>();
        for (var link : chatLinkDtoList) {
            linkResponses.add(new LinkResponse(link.getLinkId(), link.getUrl()));
        }
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    void addLinkIfNotExist(long tgChatId, URI url) {
        OffsetDateTime data = OffsetDateTime.now();
        LinkDto linkDto = new LinkDto(url, data, data);
        jdbcLinkDao.add(linkDto);

        ChatLinkDto chatLinkDto = new ChatLinkDto(tgChatId, jdbcLinkDao.getByUri(url).getFirst().getLinkId());
        jdbcChatLinkDao.add(chatLinkDto);
    }
}
