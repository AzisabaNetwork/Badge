create table player_badge_status
(
    id         int auto_increment,
    uuid       varchar(255) not null,
    badge_name varchar(255) not null,
    constraint player_badge_status_pk
        primary key (id)
);
