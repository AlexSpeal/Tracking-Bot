package edu.java.domain.implementations.jooq.repository;

import edu.java.domain.implementations.jooq.tables.ChatLink;
import edu.java.domain.implementations.jooq.tables.Link;
import edu.java.domain.interfaces.ChatLinkRepository;
import edu.java.dto.jdbc.ChatLinkDto;
import edu.java.dto.jdbc.LinkDto;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqChatLinkRepository implements ChatLinkRepository {
    @Autowired DSLContext context;

    @Transactional
    @Override
    public void add(ChatLinkDto object) {
        context.insertInto(ChatLink.CHAT_LINK).values(object.getChatId(), object.getLinkId()).execute();
    }

    @Transactional
    @Override
    public void remove(ChatLinkDto id) {
        context.deleteFrom(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.CHAT_ID.eq(id.getChatId()).and(ChatLink.CHAT_LINK.LINK_ID.eq(id.getLinkId())))
            .execute();
    }

    @Transactional
    @Override
    public List<ChatLinkDto> findAll() {
        return context.selectFrom(ChatLink.CHAT_LINK).fetchInto(ChatLinkDto.class);
    }

    @Transactional
    @Override
    public List<ChatLinkDto> getAllTgChatsByLinkId(long linkId) {
        return context.selectFrom(ChatLink.CHAT_LINK).where(ChatLink.CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(ChatLinkDto.class);
    }

    //todo может не работать
    @Transactional
    @Override
    public List<LinkDto> getAllLinkByChat(long chatId) {
        return context.selectFrom(Link.LINK)
            .where(Link.LINK.LINK_ID.in(context.select(ChatLink.CHAT_LINK.LINK_ID).from(ChatLink.CHAT_LINK)
                .where(ChatLink.CHAT_LINK.CHAT_ID.eq(chatId)))).fetchInto(LinkDto.class);
    }
}
