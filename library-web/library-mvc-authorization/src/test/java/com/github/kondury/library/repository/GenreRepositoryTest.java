package com.github.kondury.library.repository;

import com.github.kondury.library.model.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void findAll_shouldReturnListOfGenres() {
        var expected = List.of(
                "Science Fiction",
                "Detective"
        );
        assertThat(genreRepository.findAll())
                .hasSize(2)
                .extracting(Genre::getName)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void findById_shouldReturnGenreWithExpectedId() {
        var expected = 1L;
        assertThat(genreRepository.findById(expected))
                .isNotEmpty()
                .map(Genre::getId)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 3L, 1000L})
    void findById_shouldReturnEmptyOptionalIfGenreIsNotFoundById(long nonExistentId) {
        assertThat(genreRepository.findById(nonExistentId)).isNotPresent();
    }
}