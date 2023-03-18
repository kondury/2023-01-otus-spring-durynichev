package com.github.kondury.library.service;

import com.github.kondury.library.dao.BookDao;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookDao bookDao;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void insert_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        Book param = new Book(10L, "In", new Author(10L, "author"), new Genre(10L, "genre"));
        Book result = new Book(20L, "Out", new Author(20L, "author"), new Genre(20L, "genre"));

        given(bookDao.insert(any())).willReturn(result);
        assertThat(bookService.insert(param)).isEqualTo(result);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).insert(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(param);
    }

    @Test
    void update_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        Book param = new Book(10L, "In", new Author(10L, "author"), new Genre(10L, "genre"));
        Book result = new Book(20L, "Out", new Author(20L, "author"), new Genre(20L, "genre"));

        given(bookDao.update(any())).willReturn(result);
        assertThat(bookService.update(param)).isEqualTo(result);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).update(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(param);
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
