package edu.java.domain.implementations.jooq.repository;

import edu.java.domain.implementations.jooq.tables.Chat;
import edu.java.domain.interfaces.ChatRepository;
import edu.java.dto.jdbc.ChatDto;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqChatRepository implements ChatRepository {
    @Autowired DSLContext context;

    @Transactional
    @Override
    public void add(ChatDto object) {
        context.insertInto(Chat.CHAT)
            .values(object.getChatId(), object.getCreatedAt(), object.getCreatedBy(), object.getState()).execute();
    }

    @Transactional
    @Override
    public void remove(Long id) {
        context.deleteFrom(Chat.CHAT).where(Chat.CHAT.CHAT_ID.eq(id)).execute();
    }

    @Transactional
    @Override
    public void setState(Long id, String state) {
        context.update(Chat.CHAT).set(Chat.CHAT.STATE, state).where(Chat.CHAT.CHAT_ID.eq(id)).execute();
    }

    @Transactional
    @Override
    public String getState(Long id) {
        return context.select(Chat.CHAT.STATE).from(Chat.CHAT).where(Chat.CHAT.CHAT_ID.eq(id))
            .fetchOne(Chat.CHAT.STATE);
    }

    @Transactional
    @Override
    public Boolean isRegister(Long id) {
        return context.fetchExists(context.selectOne().from(Chat.CHAT).where(Chat.CHAT.CHAT_ID.eq(id)));
    }

    @Transactional
    @Override
    public List<ChatDto> findAll() {
        return context.selectFrom(Chat.CHAT).fetchInto(ChatDto.class);
    }
}
