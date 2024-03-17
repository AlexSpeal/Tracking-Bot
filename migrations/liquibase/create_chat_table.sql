create table chat
(
    chat_id              bigint not null,
    created_at      timestamp with time zone not null,
    created_by      text                     not null,
    primary key (chat_id),
    unique (chat_id)
)
