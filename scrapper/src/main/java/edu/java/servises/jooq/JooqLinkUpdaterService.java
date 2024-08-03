package edu.java.servises.jooq;

import edu.java.domain.implementations.jooq.repository.JooqLinkRepository;
import edu.java.dto.jdbc.LinkDto;
import edu.java.servises.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JooqLinkUpdaterService implements LinkUpdater {
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        jooqLinkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        jooqLinkRepository.updateCheckTime(linkId, time);
    }

    @Override
    public List<LinkDto> findOldLinksToUpdate(OffsetDateTime time) {
        return jooqLinkRepository.findOldLinksToCheck(time);
    }
}
