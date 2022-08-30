create table badges
(
    id        int auto_increment,
    file_name varchar(255) not null,
    data      mediumtext   not null,
    constraint badges_pk
        primary key (id)
);
