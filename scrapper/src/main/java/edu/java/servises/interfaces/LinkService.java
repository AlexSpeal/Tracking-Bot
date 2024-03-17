package edu.java.servises.interfaces;

import java.net.URI;
import org.example.dto.response.ListLinksResponse;

public interface LinkService {
    void add(long tgChatId, URI url);

    void remove(long tgChatId, URI url);

    ListLinksResponse listAll(long tgChatId);
}
