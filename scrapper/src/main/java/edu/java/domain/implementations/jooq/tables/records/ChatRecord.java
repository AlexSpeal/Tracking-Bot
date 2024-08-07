/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.implementations.jooq.tables.records;

import edu.java.domain.implementations.jooq.tables.Chat;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class ChatRecord extends UpdatableRecordImpl<ChatRecord>
    implements Record4<Long, OffsetDateTime, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>CHAT.CREATED_AT</code>.
     */
    public void setCreatedAt(@NotNull OffsetDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>CHAT.CREATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(1);
    }

    /**
     * Setter for <code>CHAT.CREATED_BY</code>.
     */
    public void setCreatedBy(@NotNull String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CHAT.CREATED_BY</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getCreatedBy() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CHAT.STATE</code>.
     */
    public void setState(@NotNull String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CHAT.STATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getState() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Long, OffsetDateTime, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row4<Long, OffsetDateTime, String, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Chat.CHAT.CHAT_ID;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field2() {
        return Chat.CHAT.CREATED_AT;
    }

    @Override
    @NotNull
    public Field<String> field3() {
        return Chat.CHAT.CREATED_BY;
    }

    @Override
    @NotNull
    public Field<String> field4() {
        return Chat.CHAT.STATE;
    }

    @Override
    @NotNull
    public Long component1() {
        return getChatId();
    }

    @Override
    @NotNull
    public OffsetDateTime component2() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public String component3() {
        return getCreatedBy();
    }

    @Override
    @NotNull
    public String component4() {
        return getState();
    }

    @Override
    @NotNull
    public Long value1() {
        return getChatId();
    }

    @Override
    @NotNull
    public OffsetDateTime value2() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public String value3() {
        return getCreatedBy();
    }

    @Override
    @NotNull
    public String value4() {
        return getState();
    }

    @Override
    @NotNull
    public ChatRecord value1(@NotNull Long value) {
        setChatId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord value2(@NotNull OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord value3(@NotNull String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord value4(@NotNull String value) {
        setState(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord values(
        @NotNull Long value1,
        @NotNull OffsetDateTime value2,
        @NotNull String value3,
        @NotNull String value4
    ) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    @ConstructorProperties({"chatId", "createdAt", "createdBy", "state"})
    public ChatRecord(
        @NotNull Long chatId,
        @NotNull OffsetDateTime createdAt,
        @NotNull String createdBy,
        @NotNull String state
    ) {
        super(Chat.CHAT);

        setChatId(chatId);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setState(state);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(edu.java.domain.implementations.jooq.tables.pojos.Chat value) {
        super(Chat.CHAT);

        if (value != null) {
            setChatId(value.getChatId());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setState(value.getState());
            resetChangedOnNotNull();
        }
    }
}
