--changeset kdurynichev:2023-06-07--0001-01-roles-data
insert into roles (role_name)
values ('ADMIN'),
       ('USER');


--changeset kdurynichev:2023-06-07--0001-02-users-roles-data
with users_roles_data (username, role_name) as (select *
                                                from
                                                values ('admin', 'ADMIN'),
                                                       ('user', 'USER'))
insert
into USERS_ROLES (user_id, role_id)
select user_id, role_id
from users_roles_data
         join users on users.username = users_roles_data.username
         join roles on roles.role_name = users_roles_data.role_name;

--changeset kdurynichev:2023-06-07--0001-03-privileges-data
insert into privileges (privilege_name)
values ('CREATE'),
       ('READ'),
       ('UPDATE'),
       ('DELETE');

--changeset kdurynichev:2023-06-07--0001-04-roles-privileges-data
with roles_privileges_data (role_name, privilege_name) as (select *
                                                           from
                                                           values ('ADMIN', 'CREATE'),
                                                                  ('ADMIN', 'UPDATE'),
                                                                  ('ADMIN', 'DELETE'),
                                                                  ('ADMIN', 'READ'),
                                                                  ('USER', 'READ'))
insert
into roles_privileges (role_id, privilege_id)
select role_id, privilege_id
from roles_privileges_data
         join roles on roles.role_name = roles_privileges_data.role_name
         join privileges on privileges.privilege_name = roles_privileges_data.privilege_name;

