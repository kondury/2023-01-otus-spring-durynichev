package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(JpaAuthorDao.class)
class JpaAuthorDaoTest {

    @Autowired
    private AuthorDao authorDao;

    @Test
    void findAll_shouldReturnListOfAuthors() {
        var expected = List.of(
                "Stanislaw Lem",
                "Frank Herbert",
                "Agatha Christie"
        );
        assertThat(authorDao.findAll())
                .hasSize(3)
                .extracting(Author::getName)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void findById_shouldReturnAuthorWithExpectedId() {
        var expected = 1L;
        assertThat(authorDao.findById(expected))
                .isPresent()
                .map(Author::getId)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 4L, 1000L})
    void findById_shouldReturnEmptyOptionalIfAuthorIsNotFoundById(long nonExistentId) {
        assertThat(authorDao.findById(nonExistentId)).isNotPresent();
    }

}