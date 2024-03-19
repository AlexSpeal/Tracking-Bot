create table link
(
    link_id    bigserial,
    url        text                     not null,
    updated_at timestamp with time zone not null,
    last_update timestamp with time zone not null,
    primary key (link_id),
    unique (url)
)
