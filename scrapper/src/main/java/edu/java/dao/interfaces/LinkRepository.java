package edu.java.dao.interfaces;

import edu.java.dto.jdbc.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    void add(LinkDto object);

    void remove(Long id);

    List<LinkDto> findAll();

    List<LinkDto> getByUri(URI uri);

    void updateUpdateTime(long linkId, OffsetDateTime time);

    void updateCheckTime(long linkId, OffsetDateTime time);

    List<LinkDto> findOldLinksToCheck(OffsetDateTime time);
}

