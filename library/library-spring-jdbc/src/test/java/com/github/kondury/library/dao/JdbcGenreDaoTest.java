package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreDao.class)
class JdbcGenreDaoTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    void findAll_shouldReturnListOfGenres() {
        var expected = List.of(
                "Science Fiction",
                "Detective"
        );
        assertThat(genreDao.findAll())
                .hasSize(2)
                .extracting(Genre::name)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void findById_shouldReturnGenreWithExpectedId() {
        var expected = 1L;
        assertThat(genreDao.findById(expected))
                .isNotEmpty()
                .map(Genre::id)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 3L, 1000L})
    void findById_shouldReturnEmptyOptionalIfGenreIsNotFoundById(long nonExistentId) {
        assertThat(genreDao.findById(nonExistentId)).isNotPresent();
    }
}