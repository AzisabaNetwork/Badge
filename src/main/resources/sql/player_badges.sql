create table if not exists player_badges
(
    id         int auto_increment,
    uuid       varchar(255)                       not null,
    badge_name varchar(255)                       not null,
    created_at datatime default CURRENT_TIMESTAMP not null,
    constraint player_badges_pk
        primary key (id)
);