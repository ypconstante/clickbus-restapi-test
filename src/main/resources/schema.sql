create table CLIENT_APPLICATION
(
    ID          serial primary key,
    NAME        varchar(100),
    PUBLIC_NAME varchar(255),
    CREATED_AT  datetime,
    UPDATED_AT  datetime
);

create table COUNTRY
(
    ID         serial primary key,
    NAME       varchar(100),
    CREATED_AT datetime,
    UPDATED_AT datetime
);

create table STATE
(
    ID         serial primary key,
    COUNTRY_ID long references COUNTRY (ID),
    NAME       varchar(100),
    CREATED_AT datetime,
    UPDATED_AT datetime
);

create table CITY
(
    ID         serial primary key,
    STATE_ID   long references STATE (ID),
    NAME       varchar(100),
    CREATED_AT datetime,
    UPDATED_AT datetime
);


create table PLACE
(
    ID            serial primary key,
    CITY_ID       long references CITY (ID),
    NAME          varchar(255),
    TERMINAL_NAME varchar(255),
    SLUG          varchar(255),
    ADDRESS       varchar(255),
    CREATED_AT    datetime,
    UPDATED_AT    datetime,
    constraint PLACE_SLUG_AK unique (SLUG)
);

create table PLACE_CLIENT_APPLICATION
(
    PLACE_ID  long references PLACE (ID),
    CLIENT_ID long references CLIENT_APPLICATION (ID),
    primary key (PLACE_ID, CLIENT_ID)
)
