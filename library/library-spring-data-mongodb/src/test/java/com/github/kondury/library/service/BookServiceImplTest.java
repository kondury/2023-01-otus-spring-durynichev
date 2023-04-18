package com.github.kondury.library.service;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.GenreRepository;
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
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void insert_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        String titleIn = "In";
        String authorId = "mockedAuthorId";
        String genreId = "mockerGenreId";
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);
        Book resultBook = mock(Book.class);

        given(genreRepository.findById(anyString())).willReturn(Optional.of(genre));
        given(authorRepository.findById(anyString())).willReturn(Optional.of(author));
        given(bookRepository.save(any())).willReturn(resultBook);

        assertThat(bookService.insert("In", authorId, genreId)).isEqualTo(resultBook);

        ArgumentCaptor<String> authorIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(authorRepository, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(authorId);

        ArgumentCaptor<String> genreIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(genreRepository, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(genreId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(titleIn, author, genre));
    }

    @Test
    void insert_shouldAcceptNullGenreIdAndDoNotThrowException() {
        String titleIn = "In";
        String authorId = "mockedAuthorId";
        Author author = mock(Author.class);
        Book expectedBook = mock(Book.class);

        given(authorRepository.findById(anyString())).willReturn(Optional.of(author));
        given(bookRepository.save(any())).willReturn(expectedBook);


        var resultBook = assertDoesNotThrow(() -> bookService.insert("In", authorId, null));

        assertThat(resultBook).isEqualTo(expectedBook);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(titleIn, author, null));
    }

    @Test
    void insert_shouldThrowEntityDoesNotExistExceptionWhenAuthorIdDoNotExist() {
        String mockTitle = "In";
        String authorId = "nonExistentId";
        String genreId = "genreId";

        given(authorRepository.findById(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.insert(mockTitle, authorId, genreId))
                .isInstanceOf(EntityDoesNotExist.class)
                .hasMessage("The author does not exist: authorId=nonExistentId");
    }

    @Test
    void insert_shouldThrowEntityDoesNotExistExceptionWhenGenreIdDoNotExist() {
        String mockTitle = "In";
        var authorId = "goodAuthorId";
        var genreId = "nonExistentId";

        given(genreRepository.findById(anyString())).willReturn(Optional.empty());
        given(authorRepository.findById(anyString())).willReturn(Optional.of(mock(Author.class)));

        assertThatThrownBy(() -> bookService.insert(mockTitle, authorId, genreId))
                .isInstanceOf(EntityDoesNotExist.class)
                .hasMessage("The genre does not exist: genreId=nonExistentId");
    }

    @Test
    void update_shouldPassBookToDaoAndReturnWhatDaoReturned() {
        String bookId = "mockedBookId";
        String titleIn = "In";
        String authorId = "mockedAuthorId";
        String genreId = "mockedGenreId";
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);
        Book resultBook = mock(Book.class);

        given(genreRepository.findById(anyString())).willReturn(Optional.of(genre));
        given(authorRepository.findById(anyString())).willReturn(Optional.of(author));
        given(bookRepository.existsById(anyString())).willReturn(true);
        given(bookRepository.save(any())).willReturn(resultBook);

        assertThat(bookService.update(bookId, "In", authorId, genreId)).isEqualTo(resultBook);

        ArgumentCaptor<String> authorIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(authorRepository, times(1)).findById(authorIdCaptor.capture());
        assertThat(authorIdCaptor.getAllValues()).hasSize(1).containsExactly(authorId);

        ArgumentCaptor<String> genreIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(genreRepository, times(1)).findById(genreIdCaptor.capture());
        assertThat(genreIdCaptor.getAllValues()).hasSize(1).containsExactly(genreId);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .extracting(Book::getTitle, Book::getAuthor, Book::getGenre)
                .containsExactly(tuple(titleIn, author, genre));
    }

    @Test
    void update_shouldThrowEntityDoesNotExistExceptionWhenBookIdDoesNotExist() {
        String mockedBookId = "mockedBookId";
        String mockTitle = "In";
        String mockId = "mockedId";

        given(bookRepository.existsById(anyString())).willReturn(false);
        assertThatThrownBy(() -> bookService.update(mockedBookId, mockTitle, mockId, mockId))
                .isInstanceOf(EntityDoesNotExist.class)
                .hasMessage("The book does not exist: bookId=" + mockedBookId);
    }

    @Test
    void findAll_shouldReturnWhatDaoReturned() {
        List<Book> expectedResult = List.of(
                new Book("bookId", "Book to Insert", new Author("authorId", "author"),
                        new Genre("genreId", "genre")));
        given(bookRepository.findAll()).willReturn(expectedResult);
        assertThat(bookService.findAll()).isEqualTo(expectedResult);
    }

    @Test
    void findById_shouldPassIdToDaoAndReturnWhatDaoReturned() {
        String passedId = "passedId";
        var result = Optional.of(new Book(passedId, "Out",
                new Author("authorId", "author"), new Genre("genreId", "genre")));

        given(bookRepository.findById(anyString())).willReturn(result);
        assertThat(bookService.findById(passedId)).isEqualTo(result);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(bookRepository, times(1)).findById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);
    }

    @Test
    void deleteById_shouldPassIdToDao() {
        String passedId = "passedId";

        assertDoesNotThrow(() -> bookService.deleteById(passedId));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(bookRepository, times(1)).deleteById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);
    }
}
