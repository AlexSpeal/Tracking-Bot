package edu.java.dao.interfaces;

import edu.java.dto.jdbc.ChatDto;
import edu.java.dto.jdbc.LinkDto;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    void add(LinkDto object);

    void remove(Long id);

    List<LinkDto> findAll();

    List<LinkDto> getByUri(URI uri);
}

