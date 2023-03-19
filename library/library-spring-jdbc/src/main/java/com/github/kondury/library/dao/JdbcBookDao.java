package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookDao implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;


    @Override
    public Book insert(Book book) {
        if (book.id() != 0) {
            throw new IllegalArgumentException("Book id must be equal to zero while inserting new book: book=%s".formatted(book));
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.title());
        params.addValue("author_id", book.author() != null ? book.author().id() : null);
        params.addValue("genre_id", book.genre() != null ? book.genre().id() : null);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                params,
                keyHolder,
                new String[]{"book_id"}
        );
        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue()).orElseThrow();
    }

    @Override
    public Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("book_id", book.id());
        params.addValue("title", book.title());
        params.addValue("author_id", book.author() != null ? book.author().id() : null);
        params.addValue("genre_id", book.genre() != null ? book.genre().id() : null);


        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                """
                        update books
                        set title = :title,
                            author_id = :author_id,
                            genre_id = :genre_id
                        where book_id = :book_id""",
                params,
                keyHolder,
                new String[]{"book_id"}
        );
        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue()).orElseThrow();
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query(
                """
                        select
                            books.book_id as book_id,
                            books.title as title,
                            authors.author_id as author_id,
                            authors.author_name as author_name,
                            genres.genre_id as genre_id,
                            genres.genre_name as genre_name
                        from books
                        left join authors on books.author_id = authors.author_id
                        left join genres on books.genre_id = genres.genre_id""",
                new BookMapper()
        );
    }

    @Override
    public Optional<Book> findById(long id) {
        var result = namedParameterJdbcOperations.query(
                """
                        select
                            books.book_id as book_id,
                            books.title as title,
                            authors.author_id as author_id,
                            authors.author_name as author_name,
                            genres.genre_id as genre_id,
                            genres.genre_name as genre_name
                        from books
                        left join authors on books.author_id = authors.author_id
                        left join genres on books.genre_id = genres.genre_id
                        where books.book_id = :book_id""",
                Map.of("book_id", id),
                new BookMapper()
        );
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public boolean deleteById(long id) {
        Map<String, Object> params = Map.of("book_id", id);
        var result = namedParameterJdbcOperations.update("delete from books where book_id = :book_id", params);
        return result > 0;
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("book_id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String authorName = rs.getString("author_name");
            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");
            return new Book(id, title, new Author(authorId, authorName), new Genre(genreId, genreName));
        }
    }
}
