insert into CLIENT_APPLICATION(NAME, PUBLIC_NAME, CREATED_AT, UPDATED_AT)
values ('First Client Ltda', 'The Client', '2013-10-20 10:11:12', '2019-01-05 17:59:59');

insert into COUNTRY(NAME, CREATED_AT, UPDATED_AT)
values ('Brasil', '2013-08-01 08:01:13', '2013-08-01 08:01:14');

insert into STATE(COUNTRY_ID, NAME, CREATED_AT, UPDATED_AT)
values (1, 'Santa Catarina', '2013-08-02 14:15:16', '2013-08-02 14:15:17');

insert into CITY(STATE_ID, NAME, CREATED_AT, UPDATED_AT)
values (1, 'Floripa', '2013-08-02 14:23:01', '2013-08-02 14:23:02');

insert into PLACE(CITY_ID, NAME, TERMINAL_NAME, SLUG, ADDRESS, CREATED_AT, UPDATED_AT)
values (1, 'Terminal Rita Maria', 'A1', 'terminal-rita-maria', 'Rua do Terminal', '2013-08-02 14:28:42', '2013-08-02 14:28:43');
