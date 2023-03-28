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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void insert_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        String titleIn = "In";
        long authorId = 10L;
        long genreId = 10L;
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);
        Book resultBook = mock(Book.class);

        given(genreDao.findById(anyLong())).willReturn(Optional.of(genre));
        given(authorDao.findById(anyLong())).willReturn(Optional.of(author));
        given(bookDao.save(any())).willReturn(resultBook);

        assertThat(bookService.insert("In", authorId, genreId)).isEqualTo(Optional.of(resultBook));
        
        ArgumentCaptor<Long> authorIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorDao, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(authorId);

        ArgumentCaptor<Long> genreIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(genreDao, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(genreId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(titleIn, author, genre));

    }

    @Test
    void update_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        long bookId = 1L;
        String titleIn = "In";
        long authorId = 10L;
        long genreId = 10L;
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);
        Book resultBook = mock(Book.class);

        given(genreDao.findById(anyLong())).willReturn(Optional.of(genre));
        given(authorDao.findById(anyLong())).willReturn(Optional.of(author));
        given(bookDao.save(any())).willReturn(resultBook);

        assertThat(bookService.update(bookId, "In", authorId, genreId)).isEqualTo(Optional.of(resultBook));

        ArgumentCaptor<Long> authorIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorDao, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(authorId);

        ArgumentCaptor<Long> genreIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(genreDao, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(genreId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(titleIn, author, genre));
    }

    @Test
    void findAll_shouldReturnWhatDaoReturned() {
        List<Book> expectedResult = List.of(
                new Book(10L, "Book to Insert", new Author(10L, "author"), new Genre(10L, "genre"))
        );
        given(bookDao.findAllCompleted()).willReturn(expectedResult);
        assertThat(bookService.findAll()).isEqualTo(expectedResult);
    }

    @Test
    void findById_shouldPassIdToDaoAndReturnWhatDaoReturned() {
        long passedId = 10L;
        var result = Optional.of(new Book(passedId, "Out", new Author(1L, "author"), new Genre(1L, "genre")));

        given(bookDao.findByIdCompleted(anyLong())).willReturn(result);
        assertThat(bookService.findById(passedId)).isEqualTo(result);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(bookDao, times(1)).findByIdCompleted(captor.capture());
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
