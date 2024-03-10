create table link
(
    link_id              bigint generated always as identity,
    url            text                     not null,
    updated_at      timestamp with time zone not null,
    primary key (link_id)
)
