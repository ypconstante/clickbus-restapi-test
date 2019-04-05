create table CLIENT_APPLICATION
(
    ID          integer not null auto_increment unique,
    NAME        varchar(100),
    PUBLIC_NAME varchar(255),
    CREATED_AT  datetime,
    UPDATED_AT  datetime,
    primary key (ID)
);

create table COUNTRY
(
    ID         integer not null auto_increment unique,
    NAME       varchar(100),
    CREATED_AT datetime,
    UPDATED_AT datetime,
    primary key (ID)
);

create table STATE
(
    ID         integer not null auto_increment unique,
    COUNTRY_ID integer references COUNTRY (ID),
    NAME       varchar(100),
    CREATED_AT datetime,
    UPDATED_AT datetime,
    primary key (ID)
);

create table CITY
(
    ID         integer not null auto_increment unique,
    STATE_ID   integer references STATE (ID),
    NAME       varchar(100),
    CREATED_AT datetime,
    UPDATED_AT datetime,
    primary key (ID)
);


create table PLACE
(
    ID            integer not null auto_increment unique,
    CITY_ID       integer references CITY (ID),
    NAME          varchar(255),
    TERMINAL_NAME varchar(255),
    SLUG          varchar(255),
    ADDRESS       varchar(255),
    CREATED_AT    datetime,
    UPDATED_AT    datetime,
    primary key (ID),
    constraint PLACE_SLUG_AK unique (SLUG)
);

create table PLACE_CLIENT_APPLICATION
(
    PLACE_ID  integer references PLACE (ID),
    CLIENT_ID integer references CLIENT_APPLICATION (ID),
    primary key (PLACE_ID, CLIENT_ID)
)
