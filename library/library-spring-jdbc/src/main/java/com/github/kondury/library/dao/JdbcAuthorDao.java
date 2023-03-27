package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorDao implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final RowMapper<Author> mapper = (resultSet, rowNum) -> {
        long id = resultSet.getLong("author_id");
        String name = resultSet.getString("author_name");
        return new Author(id, name);
    };

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("select author_id, author_name from authors", mapper);
    }

    @Override
    public Optional<Author> findById(long id) {
        var result = namedParameterJdbcOperations.query(
                "select author_id, author_name from authors where author_id = :author_id",
                Map.of("author_id", id),
                mapper
        );
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

}