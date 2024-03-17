package edu.java.servises.interfaces;

import edu.java.dto.jdbc.LinkDto;
import org.example.dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;

public interface LinkService {
    void add(long tgChatId, URI url);
    void remove(long tgChatId, URI url);
    ListLinksResponse listAll(long tgChatId);
}
