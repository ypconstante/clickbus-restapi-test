insert into CLIENT_APPLICATION(NAME, PUBLIC_NAME, CREATED_AT, UPDATED_AT)
values ('First Client Ltda', 'The Client', '2013-10-20 10:11:12', '2019-01-05 17:59:59'),
       ('Client 02', 'Client 02 Public Name', current_timestamp(), current_timestamp()),
       ('Client 03', 'Client 03 Public Name', current_timestamp(), current_timestamp()),
       ('Client 04', 'Client 04 Public Name', current_timestamp(), current_timestamp()),
       ('Client 05', 'Client 05 Public Name', current_timestamp(), current_timestamp());

insert into COUNTRY(NAME, CREATED_AT, UPDATED_AT)
values ('Brasil', '2013-08-01 08:01:13', '2013-08-01 08:01:14'),
       ('Portugal', current_timestamp(), current_timestamp());

insert into STATE(COUNTRY_ID, NAME, CREATED_AT, UPDATED_AT)
values (1, 'Santa Catarina', '2013-08-02 14:15:16', '2013-08-02 14:15:17'),
       (1, 'São Paulo', current_timestamp(), current_timestamp()),
       (2, 'Lisboa', current_timestamp(), current_timestamp()),
       (2, 'Porto', current_timestamp(), current_timestamp());

insert into CITY(STATE_ID, NAME, CREATED_AT, UPDATED_AT)
values (1, 'Floripa', '2013-08-02 14:23:01', '2013-08-02 14:23:02'),
       (1, 'Joinville', current_timestamp(), current_timestamp()),
       (2, 'São Paulo', current_timestamp(), current_timestamp()),
       (2, 'Santos', current_timestamp(), current_timestamp()),
       (3, 'Arruda dos Vinhos', current_timestamp(), current_timestamp()),
       (3, 'Cascais', current_timestamp(), current_timestamp()),
       (4, 'Penafiel', current_timestamp(), current_timestamp()),
       (4, 'Santo Tirso', current_timestamp(), current_timestamp());

insert into PLACE(CITY_ID, NAME, TERMINAL_NAME, SLUG, ADDRESS, CREATED_AT, UPDATED_AT)
values (1, 'Terminal Rita Maria', 'A01', 'terminal-rita-maria', 'Rua do Terminal', '2013-08-02 14:28:42', '2013-08-02 14:28:43'),
       (1, 'Terminal Floripa 02', 'A02', 'terminal-02', 'Rua 02', current_timestamp(), current_timestamp()),
       (2, 'Terminal Joinville 03', 'A03', 'terminal-03', 'Rua 03', current_timestamp(), current_timestamp()),
       (2, 'Terminal Joinville 04', 'A04', 'terminal-04', 'Rua 04', current_timestamp(), current_timestamp()),
       (3, 'Terminal São Paulo 05', 'A05', 'terminal-05', 'Rua 05', current_timestamp(), current_timestamp()),
       (3, 'Terminal São Paulo 06', 'A06', 'terminal-06', 'Rua 06', current_timestamp(), current_timestamp()),
       (4, 'Terminal Santos 07', 'A07', 'terminal-07', 'Rua 07', current_timestamp(), current_timestamp()),
       (4, 'Terminal Santos 08', 'A08', 'terminal-08', 'Rua 08', current_timestamp(), current_timestamp()),
       (5, 'Terminal Arruda dos Vinhos 09', 'A09', 'terminal-09', 'Rua 09', current_timestamp(), current_timestamp()),
       (5, 'Terminal Arruda dos Vinhos 10', 'A10', 'terminal-10', 'Rua 10', current_timestamp(), current_timestamp()),
       (6, 'Terminal Cascais 11', 'A11', 'terminal-11', 'Rua 11', current_timestamp(), current_timestamp()),
       (6, 'Terminal Cascais 12', 'A12', 'terminal-12', 'Rua 12', current_timestamp(), current_timestamp()),
       (7, 'Terminal Penafiel 13', 'A13', 'terminal-13', 'Rua 13', current_timestamp(), current_timestamp()),
       (7, 'Terminal Penafiel 14', 'A14', 'terminal-14', 'Rua 14', current_timestamp(), current_timestamp()),
       (8, 'Terminal Santo Tirso 15', 'A15', 'terminal-15', 'Rua 15', current_timestamp(), current_timestamp()),
       (8, 'Terminal Santo Tirso 16', 'A16', 'terminal-16', 'Rua 16', current_timestamp(), current_timestamp());
