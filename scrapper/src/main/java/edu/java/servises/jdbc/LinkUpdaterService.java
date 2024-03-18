package edu.java.servises.jdbc;

import edu.java.dao.implementations.JdbcChatLinkDao;
import edu.java.dao.implementations.JdbcLinkDao;
import edu.java.dto.jdbc.LinkDto;
import edu.java.servises.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdaterService implements LinkUpdater {
    @Autowired
    private JdbcLinkDao jdbcLinkDao;
    @Autowired
    private JdbcChatLinkDao jdbcChatLinkDao;

    @Override
    public void update(long linkId, OffsetDateTime time) {
        jdbcLinkDao.updateUpdateTime(linkId, time);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        jdbcLinkDao.updateCheckTime(linkId, time);
    }

    @Override
    public List<LinkDto> findOldLinksToUpdate(OffsetDateTime time) {
        return jdbcLinkDao.findOldLinksToCheck(time);
    }

}
