package edu.java.servises.jpa;

import edu.java.domain.implementations.jpa.JpaLinkRepository;
import edu.java.dto.jdbc.LinkDto;
import edu.java.servises.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JpaLinkUpdaterService implements LinkUpdater {
    @Autowired
    private JpaLinkRepository jpaLinkService;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        jpaLinkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long linkId, OffsetDateTime time) {
        jpaLinkRepository.updateCheckTime(linkId, time);

    }

    @Override
    public List<LinkDto> findOldLinksToUpdate(OffsetDateTime time) {
        return jpaLinkRepository.findAllByLastUpdateBefore(time).stream().map(LinkDto::new).toList();
    }
}
