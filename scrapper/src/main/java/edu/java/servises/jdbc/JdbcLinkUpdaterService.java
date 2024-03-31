package edu.java.servises.jdbc;

import edu.java.domain.implementations.jdbc.JdbcLinkRepository;
import edu.java.dto.jdbc.LinkDto;
import edu.java.servises.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdater {
    private final JdbcLinkRepository jdbcLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        jdbcLinkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        jdbcLinkRepository.updateCheckTime(linkId, time);
    }

    @Override
    public List<LinkDto> findOldLinksToUpdate(OffsetDateTime time) {
        return jdbcLinkRepository.findOldLinksToCheck(time);
    }

}
