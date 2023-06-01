--changeset kdurynichev:2023-05-28--0001-users-data
insert into users (username, password)
values ('admin', '{bcrypt}$2a$10$.57y.iT5LkecstsZJ5NZdOlyt.p4crgegJ2ZkXxzA0ljWn9gbJqPu'),
       ('user', '{bcrypt}$2a$10$fqZbafFOkcFe/gD9U883kOT6mdgC7JN8enBcjz77N3PmeYB1El3He')