--changeset kdurynichev:2023-03-23--0001-comments-data
with comments_data (title, comment_text) as
         (select *
          from
          values
/*
                 ('Solaris', 'Have already read it twice and will do more'),
*/
              ('Dune', 'It''s the real masterpiece!!! Must read!!!'),
              ('Dune', 'As for me it was a little bit too lingering and somewhat boring'),
              ('Murder on the Orient Express', 'Classics means classics! Neither more nor less'))
insert
into comments (book_id, comment_text)
select books.book_id, comments_data.comment_text
from comments_data
         join books on books.title = comments_data.title;
