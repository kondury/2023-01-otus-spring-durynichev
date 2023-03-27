package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Genre;
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
public class JdbcGenreDao implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select genre_id, genre_name from genres", new GenreMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        var result = namedParameterJdbcOperations.query(
                "select genre_id, genre_name from genres where genre_id = :genre_id",
                Map.of("genre_id", id),
                new GenreMapper()
        );
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            long id = resultSet.getLong("genre_id");
            String name = resultSet.getString("genre_name");
            return new Genre(id, name);
        }
    }

}

