package com.github.kondury.library.service;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.GenreRepository;
import com.github.kondury.library.service.dto.*;
import com.github.kondury.library.service.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void insert_shouldPassCreateRequestToDaoAndReturnWhatDaoReturned() {
        CreateBookRequest request = new CreateBookRequest("In", 10L, 10L);
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);
        Book resultBook = mock(Book.class);
        BookDto resultBookDto = new BookDto(1L, "In",
                new AuthorDto(10L, "Author"),
                new GenreDto(10L, "Genre"));

        given(genreRepository.findById(anyLong())).willReturn(Optional.of(genre));
        given(authorRepository.findById(anyLong())).willReturn(Optional.of(author));
        given(bookRepository.save(any())).willReturn(resultBook);

        given(bookMapper.bookToBookDto(any())).willReturn(resultBookDto);

        assertThat(bookService.insert(request)).isEqualTo(resultBookDto);

        ArgumentCaptor<Long> authorIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(request.authorId());

        ArgumentCaptor<Long> genreIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(genreRepository, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(request.genreId());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(request.title(), author, genre));
    }

    @Test
    void update_shouldPassUpdateRequestToDaoAndReturnWhatDaoReturned() {

        UpdateBookRequest request = new UpdateBookRequest(1L, "In", 10L, 10L);
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);
        Book resultBook = mock(Book.class);
        BookDto resultBookDto = new BookDto(1L, "In",
                new AuthorDto(10L, "Author"),
                new GenreDto(10L, "Genre"));

        given(genreRepository.findById(anyLong())).willReturn(Optional.of(genre));
        given(authorRepository.findById(anyLong())).willReturn(Optional.of(author));
        given(bookRepository.existsById(anyLong())).willReturn(true);
        given(bookRepository.save(any())).willReturn(resultBook);
        given(bookMapper.bookToBookDto(any())).willReturn(resultBookDto);

        assertThat(bookService.update(request)).isEqualTo(resultBookDto);

        ArgumentCaptor<Long> authorIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(request.authorId());

        ArgumentCaptor<Long> genreIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(genreRepository, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(request.genreId());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(request.title(), author, genre));
    }

    @Test
    void update_shouldThrowEntityDoesNotExistExceptionIfBookIdDoesNotExist() {
        long mockedBookId = 1L;
        var updateRequest = new UpdateBookRequest(mockedBookId, "In", 10L, 10L);

        given(bookRepository.existsById(mockedBookId)).willReturn(false);
        assertThatThrownBy(() -> bookService.update(updateRequest))
                .isExactlyInstanceOf(EntityDoesNotExistException.class);
    }

    @Test
    void findAll_shouldCallRepositoryMethodAndPassDaoResultToMapper() {
        List<Book> booksFromDao = List.of(
                new Book(10L, "Book 1", new Author(10L, "Author 1"), new Genre(10L, "Genre 1")),
                new Book(20L, "Book 2", new Author(10L, "Author 2"), new Genre(10L, "Genre 2"))
        );
        var mock = new BookDto(null, null, null, null);

        given(bookRepository.findAll()).willReturn(booksFromDao);
        given(bookMapper.bookToBookDto(any())).willReturn(mock, mock);

        assertThat(bookService.findAll()).hasSize(2).containsExactly(mock, mock);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookMapper, times(2)).bookToBookDto(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(2)
                .extracting(Book::getTitle,
                        it -> it.getAuthor().getName(),
                        it -> it.getGenre().getName())
                .containsExactly(tuple("Book 1", "Author 1", "Genre 1"),
                        tuple("Book 2", "Author 2", "Genre 2"));
    }

    @Test
    void findById_shouldPassIdToDaoAndReturnWhatDaoReturned() {
        long passedId = 10L;
        var book = new Book(passedId, "Out", new Author(1L, "author"), new Genre(1L, "genre"));
        var mock = new BookDto(null, null, null, null);

        given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
        given(bookMapper.bookToBookDto(any())).willReturn(mock);
        assertThat(bookService.findById(passedId)).isEqualTo(Optional.of(mock));

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository, times(1)).findById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookMapper, times(1)).bookToBookDto(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle,
                        it -> it.getAuthor().getName(),
                        it -> it.getGenre().getName())
                .containsExactly(tuple("Out", "author", "genre"));
    }

    @Test
    void deleteById_shouldPassIdToDao() {
        long passedId = 10L;

        assertDoesNotThrow(() -> bookService.deleteById(passedId));

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository, times(1)).deleteById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);
    }
}
