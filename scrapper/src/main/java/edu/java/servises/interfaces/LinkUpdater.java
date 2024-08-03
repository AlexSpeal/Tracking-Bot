package edu.java.servises.interfaces;

import edu.java.dto.jdbc.LinkDto;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void update(long linkId, OffsetDateTime time, String data);

    void check(long linkId, OffsetDateTime time);

    List<LinkDto> findOldLinksToUpdate(OffsetDateTime time);
}
