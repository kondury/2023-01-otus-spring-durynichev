--changeset kdurynichev:2023-03-10--0001-authors-data
insert into authors (author_name)
values ('Stanislaw Lem'),
       ('Frank Herbert'),
       ('Agatha Christie');

--changeset kdurynichev:2023-03-10--0002-genres-data
insert into genres (genre_name)
values ('Science Fiction'),
       ('Detective');

--changeset kdurynichev:2023-03-10--0003-books-data
with book_data (title, author_name, genre_name) as (select *
                                                    from
                                                    values ('Solaris', 'Stanislaw Lem', 'Science Fiction'),
                                                           ('Dune', 'Frank Herbert', 'Science Fiction'),
                                                           ('Murder on the Orient Express', 'Agatha Christie',
                                                            'Detective'))
insert
into books (title, author_id, genre_id)
select book_data.title, authors.author_id, genres.genre_id
from book_data
         join authors on authors.author_name = book_data.author_name
         join genres on genres.genre_name = book_data.genre_name;
