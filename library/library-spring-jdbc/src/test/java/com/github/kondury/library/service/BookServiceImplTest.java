package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorDao;
import com.github.kondury.library.dao.BookDao;
import com.github.kondury.library.dao.GenreDao;
import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookDao bookDao;
    @Mock
    AuthorDao authorDao;
    @Mock
    GenreDao genreDao;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void insert_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        String titleIn = "In";
        long authorId = 10L;
        long genreId = 10L;
        Author author = new Author(authorId, "author");
        Genre genre = new Genre(genreId, "genre");
        Book resultBook = new Book(titleIn, author, genre);

        given(genreDao.findById(anyLong())).willReturn(Optional.of(genre));
        given(authorDao.findById(anyLong())).willReturn(Optional.of(author));
        given(bookDao.insert(any())).willReturn(resultBook);

        assertThat(bookService.insert("In", authorId, genreId)).isEqualTo(resultBook);

        ArgumentCaptor<Long> authorIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorDao, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(authorId);

        ArgumentCaptor<Long> genreIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(genreDao, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(genreId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).insert(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::title, Book::author, Book::genre)
                .containsExactly(tuple(titleIn, author, genre));

    }

    @Test
    void update_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        long bookId = 1L;
        String titleIn = "In";
        long authorId = 10L;
        long genreId = 10L;
        Author author = new Author(authorId, "author");
        Genre genre = new Genre(genreId, "genre");
        Book resultBook = new Book(titleIn, author, genre);

        given(genreDao.findById(anyLong())).willReturn(Optional.of(genre));
        given(authorDao.findById(anyLong())).willReturn(Optional.of(author));
        given(bookDao.update(any())).willReturn(resultBook);

        assertThat(bookService.update(bookId, "In", authorId, genreId)).isEqualTo(resultBook);

        ArgumentCaptor<Long> authorIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorDao, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(authorId);

        ArgumentCaptor<Long> genreIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(genreDao, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(genreId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).update(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::title, Book::author, Book::genre)
                .containsExactly(tuple(titleIn, author, genre));
    }

    @Test
    void findAll_shouldReturnWhatDaoReturned() {
        List<Book> expectedResult = List.of(
                new Book(10L, "Book to Insert", new Author(10L, "author"), new Genre(10L, "genre"))
        );
        given(bookDao.findAll()).willReturn(expectedResult);
        assertThat(bookService.findAll()).isEqualTo(expectedResult);
    }

    @Test
    void findById_shouldPassIdToDaoAndReturnWhatDaoReturned() {
        long passedId = 10L;
        var result = Optional.of(new Book(passedId, "Out", new Author(1L, "author"), new Genre(1L, "genre")));

        given(bookDao.findById(anyLong())).willReturn(result);
        assertThat(bookService.findById(passedId)).isEqualTo(result);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(bookDao, times(1)).findById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);
    }

    @Test
    void deleteById_shouldPassIdToDao() {
        long passedId = 10L;

        assertDoesNotThrow(() -> bookService.deleteById(passedId));

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(bookDao, times(1)).deleteById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);
    }
}
