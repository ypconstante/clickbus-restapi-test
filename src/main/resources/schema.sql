create table client_application
(
    id          integer not null auto_increment unique,
    name        varchar(100),
    public_name varchar(255),
    created_at  datetime,
    updated_at  datetime,
    primary key (id)
);

create table country
(
    id         integer not null auto_increment unique,
    name       varchar(100),
    created_at datetime,
    updated_at datetime,
    primary key (id)
);

create table state
(
    id         integer not null auto_increment unique,
    country_id integer references country (id),
    name       varchar(100),
    created_at datetime,
    updated_at datetime,
    primary key (id)
);

create table city
(
    id         integer not null auto_increment unique,
    state_id   integer references state (id),
    name       varchar(100),
    created_at datetime,
    updated_at datetime,
    primary key (id)
);


create table place
(
    id            integer not null auto_increment unique,
    city_id       integer references city (id),
    name          varchar(255),
    terminal_name varchar(255),
    slug          varchar(255),
    address       varchar(255),
    created_at    datetime,
    updated_at    datetime,
    primary key (id),
    constraint place_slug_ak unique (slug)
);

create table place_client_application
(
    place_id  integer references place (id),
    client_id integer references client_application (id),
    primary key (place_id, client_id)
)
