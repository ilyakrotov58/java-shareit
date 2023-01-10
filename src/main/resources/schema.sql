create sequence if not exists "Item_id_seq"
    as integer;

create sequence if not exists user_id_seq
    as integer;

create table if not exists users
(
    id        integer default nextval('user_id_seq') not null
        constraint user_pk
            primary key,
    email     varchar                                          not null,
    user_name varchar                                          not null,
    last_name varchar
);

create unique index if not exists user_email_uindex
    on users (email);

create unique index if not exists user_id_uindex
    on users (id);

create table if not exists items
(
    id           integer default nextval('"Item_id_seq"') not null
        constraint item_pk
            primary key,
    item_name    varchar                                            not null,
    description  varchar(200)                                       not null,
    availability boolean                                            not null,
    user_id      integer                                            not null
        constraint items_users_id_fk
            references users
            on update cascade on delete cascade
);

create unique index if not exists item_id_uindex
    on items (id);

create table if not exists bookings
(
    id             serial
        constraint bookings_pk
            primary key,
    booker_id      integer not null
        constraint bookings_users_id_fk
            references users
            on update cascade on delete cascade,
    booking_status varchar not null,
    start_date     timestamp,
    end_date       timestamp,
    item_id        integer
        constraint bookings_items_id_fk
            references items
            on update cascade on delete cascade
);

create unique index if not exists bookings_id_uindex
    on bookings (id);

create table if not exists comments
(
    id         serial
        constraint comments_pk
            primary key,
    text       text    not null,
    author_id  integer not null
        constraint comments_users_id_fk
            references users
            on update cascade on delete cascade,
    item_id    integer not null
        constraint comments_items_id_fk
            references items
            on update cascade on delete cascade,
    created_at timestamp
);

create unique index if not exists comments_id_uindex
    on comments (id);

