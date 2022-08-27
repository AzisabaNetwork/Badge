create table if not exists player_badges
(
    id         int auto_increment,
    uuid       VARCHAR(255)                       not null,
    badge_name VARCHAR(255)                       not null,
    created_at DATETIME default CURRENT_TIMESTAMP not null,
    constraint player_badges_pk
        primary key (id)
);