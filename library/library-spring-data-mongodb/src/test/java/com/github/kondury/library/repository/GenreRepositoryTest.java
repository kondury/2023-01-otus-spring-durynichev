package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
//@Import(MongoGenreCascadeDeleteEventsListener.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void deleteById_shouldRemoveGenreFromAllBooksWhenDeletingGenre() {

        val genreToDelete = mongoTemplate.findAll(Book.class).get(0).getGenre();
        val expectedGenresCount = mongoTemplate.findAll(Genre.class).size() - 1;

        genreRepository.deleteById(genreToDelete.getId());

        assertThat(mongoTemplate.findAll(Genre.class))
                .hasSize(expectedGenresCount)
                .extracting(Genre::getName)
                .doesNotContain(genreToDelete.getName());

        assertThat(mongoTemplate.findAll(Book.class))
                .extracting(Book::getGenre)
                .filteredOn(Objects::nonNull)
                .extracting(Genre::getName)
                .doesNotContain(genreToDelete.getName());
    }
}