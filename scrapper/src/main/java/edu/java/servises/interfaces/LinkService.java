package edu.java.servises.interfaces;

import java.net.URI;
import java.util.List;
import org.example.dto.response.ListLinksResponse;

public interface LinkService {
    void add(long tgChatId, URI url);

    void remove(long tgChatId, URI url);

    List<Long> getLinkedChadId(long linkId);

    ListLinksResponse listAll(long tgChatId);
}
