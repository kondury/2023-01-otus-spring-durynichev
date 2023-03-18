package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorDao implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("select author_id, author_name from authors", new AuthorMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var result = namedParameterJdbcOperations.query(
                "select author_id, author_name from authors where author_id = :author_id",
                Map.of("author_id", id),
                new AuthorMapper()
        );
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            long id = resultSet.getLong("author_id");
            String name = resultSet.getString("author_name");
            return new Author(id, name);
        }
    }
}