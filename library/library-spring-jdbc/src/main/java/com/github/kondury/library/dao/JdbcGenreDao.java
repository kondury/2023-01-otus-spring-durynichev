package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreDao implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final RowMapper<Genre> mapper = (resultSet, rowNum) -> {
        long id = resultSet.getLong("genre_id");
        String name = resultSet.getString("genre_name");
        return new Genre(id, name);
    };

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select genre_id, genre_name from genres", mapper);
    }

    @Override
    public Optional<Genre> findById(long id) {
        var result = namedParameterJdbcOperations.query(
                "select genre_id, genre_name from genres where genre_id = :genre_id",
                Map.of("genre_id", id),
                mapper
        );
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }
}

