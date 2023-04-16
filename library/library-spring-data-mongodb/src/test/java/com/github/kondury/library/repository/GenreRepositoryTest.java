package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
//@Import(MongoGenreCascadeDeleteEventsListener.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void deleteById_shouldRemoveGenreFromAllBooksWhenDeletingGenre() {

        val books = bookRepository.findAll();
        val book = books.get(0);
        val genreToDelete = book.getGenre();
        var genres = genreRepository.findAll();

        genreRepository.deleteById(genreToDelete.getId());

        val expectedGenresCount = genres.size() - 1;
        assertThat(genreRepository.findAll())
                .hasSize(expectedGenresCount)
                .extracting(Genre::getName)
                .doesNotContain(genreToDelete.getName());

        assertThat(bookRepository.findAll())
                .extracting(Book::getGenre)
                .filteredOn(Objects::nonNull)
                .extracting(Genre::getName)
                .doesNotContain(genreToDelete.getName());
    }
}