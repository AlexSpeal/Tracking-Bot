package edu.java.domain.implementations.jooq.repository;

import edu.java.domain.implementations.jooq.tables.Link;
import edu.java.domain.interfaces.LinkRepository;
import edu.java.dto.jdbc.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqLinkRepository implements LinkRepository {
    @Autowired DSLContext context;

    @Transactional
    @Override
    public void add(LinkDto object) {
        context.insertInto(Link.LINK).set(Link.LINK.URL, object.getUrl().toString())
            .set(Link.LINK.UPDATED_AT, object.getUpdatedAt()).set(Link.LINK.LAST_UPDATE, object.getLastUpdate())
            .set(Link.LINK.TYPE, object.getType()).set(Link.LINK.DATA, object.getData()).execute();
    }

    @Transactional
    @Override
    public void remove(Long id) {
        context.deleteFrom(Link.LINK).where(Link.LINK.LINK_ID.eq(id)).execute();
    }

    @Transactional
    @Override
    public List<LinkDto> findAll() {
        return context.selectFrom(Link.LINK).fetchInto(LinkDto.class);
    }

    @Transactional
    @Override
    public List<LinkDto> getByUri(URI uri) {
        return context.selectFrom(Link.LINK).where(Link.LINK.URL.eq(uri.toString())).fetchInto(LinkDto.class);
    }

    @Transactional
    @Override
    public void updateData(long linkId, OffsetDateTime time, String data) {
        context.update(Link.LINK).set(Link.LINK.DATA, data).where(Link.LINK.LINK_ID.eq(linkId)).execute();
    }

    @Transactional
    @Override
    public void updateCheckTime(long linkId, OffsetDateTime time) {
        context.update(Link.LINK).set(Link.LINK.LAST_UPDATE, time).where(Link.LINK.LINK_ID.eq(linkId)).execute();
    }

    @Transactional
    @Override
    public List<LinkDto> findOldLinksToCheck(OffsetDateTime time) {
        return context.selectFrom(Link.LINK).where(Link.LINK.LAST_UPDATE.lt(time)).fetchInto(LinkDto.class);
    }
}
