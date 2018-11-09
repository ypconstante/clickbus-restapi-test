create table country(
id serial,
name varchar(100),
created_at timestamp,
updated_at timestamp,
primary key (id));

create table state(
id serial,
country_id integer,
name varchar(100),
created_at timestamp,
updated_at timestamp,
primary key(id),
foreign key(country_id) references country(id));

create table city(
id serial,
state_id integer,
name varchar(100),
created_at timestamp,
updated_at timestamp,
primary key(id),
foreign key(state_id) references state(id));

create table place(
id serial,
name varchar(255),
terminal_name varchar(255),
slug varchar(255) unique,
adress varchar(255),
city_id integer,
created_at timestamp,
updated_at timestamp,
primary key(id),
foreign key(city_id) references city(id));

create table client_application(
id serial,
name varchar(100),
public_name varchar(255),
created_at timestamp,
updated_at timestamp,
primary key(id));

create table place_client_application(
id serial,
place_id integer,
client_id integer,
primary key (id),
foreign key(place_id) references place(id),
foreign key(client_id) references client_application(id));
