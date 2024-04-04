package edu.java.servises.jpa;

import edu.java.domain.implementations.jpa.JpaChatRepository;
import edu.java.domain.implementations.jpa.JpaLinkRepository;
import edu.java.dto.jdbc.github.Github;
import edu.java.dto.jdbc.stackOverflow.Question;
import edu.java.entities.ChatEntity;
import edu.java.entities.LinkEntity;
import edu.java.servises.handlers.GithubHandler;
import edu.java.servises.handlers.StackOverflowHandler;
import edu.java.servises.interfaces.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import errors.LinkWasNotTrackedException;
import org.example.dto.response.LinkResponse;
import org.example.dto.response.ListLinksResponse;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JpaLinkService implements LinkService {
    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;
    private final GithubHandler githubHandler;
    private final StackOverflowHandler stackOverflowHandler;
    private final static String GITHUB = "github";
    private final static String STACKOVERFLOW = "stackoverflow";

    @Override
    public void add(long tgChatId, URI url) {
        ChatEntity chat = jpaChatRepository.findById(tgChatId).orElseThrow();

        if (!jpaLinkRepository.existsByUrl(url.toString())) {
            addLinkIfNotExist(tgChatId, url);
        } else {
            chat.getLinks().add(jpaLinkRepository.findByUrl(url.toString()));
        }

        jpaChatRepository.flush();

    }

    void addLinkIfNotExist(long tgChatId, URI url) {
        OffsetDateTime data = OffsetDateTime.now();
        String type = getType(url);
        String dataRep = getDate(type, url);
        ChatEntity chat = jpaChatRepository.findById(tgChatId).orElseThrow();
        LinkEntity linkEntity = new LinkEntity(url.toString(), data, data, type, dataRep, Set.of(chat));
        chat.getLinks().add(linkEntity);
        jpaLinkRepository.save(linkEntity);

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

    @Override
    public void remove(long tgChatId, URI url) {
        ChatEntity chat = jpaChatRepository.findById(tgChatId).orElseThrow();
        Set<ChatEntity> chats = jpaLinkRepository.findByUrl(url.toString()).getChats();
        LinkEntity link = jpaLinkRepository.findByUrl(url.toString());
        if (!chats.contains(chat)) {
            throw new LinkWasNotTrackedException("Ссылка не отслеживается!");
        }
        chat.getLinks().remove(link);
        if (jpaLinkRepository.findByUrl(url.toString()).getChats().size() == 1) {
            jpaLinkRepository.deleteById(link.getLinkId());
        }
        jpaLinkRepository.flush();
        jpaChatRepository.flush();
    }

    @Override
    public List<Long> getLinkedChadId(long linkId) {
        return jpaLinkRepository.findById(linkId).orElseThrow().getChats().stream().map(ChatEntity::getChatId).toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<LinkResponse> linkResponses = new ArrayList<>();
        var linksByChat = jpaChatRepository.findById(tgChatId).orElseThrow().getLinks();
        for (var link : linksByChat) {
            try {
                linkResponses.add(new LinkResponse(link.getLinkId(), new URI(link.getUrl())));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
