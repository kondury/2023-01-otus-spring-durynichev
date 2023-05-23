package com.github.kondury.library.rest;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.repository.GenreRepository;
import com.github.kondury.library.rest.dto.BookDto;
import com.github.kondury.library.rest.dto.CreateBookRequest;
import com.github.kondury.library.rest.dto.UpdateBookRequest;
import com.github.kondury.library.rest.mapper.AuthorMapperImpl;
import com.github.kondury.library.rest.mapper.BookMapper;
import com.github.kondury.library.rest.mapper.BookMapperImpl;
import com.github.kondury.library.rest.mapper.GenreMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@WebFluxTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private CommentRepository commentRepository;
    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private WebTestClient webClient;

    private static final String TITLE = "title";
    private static final String AUTHOR_ID = "authorId";
    private static final String GENRE_ID = "genreId";
    private static final String BOOK_ID = "bookId";
    private static final String NON_EXISTENT_ID = "nonExistentId";
    private static final Author AUTHOR = new Author(AUTHOR_ID, "Author");
    private static final Genre GENRE = new Genre(GENRE_ID, "Genre");
    private static final Book BOOK = new Book(BOOK_ID, TITLE, AUTHOR, GENRE);


    @Test
    void findAll_shouldReturnBooksReturnedByRepository() {

        var author1 = new Author("authorId1", "Author1");
        var author2 = new Author("authorId2", "Author2");
        var genre1 = new Genre("genreId1", "Genre1");
        var genre2 = new Genre("genreId2", "Genre2");
        var allBooks = List.of(
                new Book("bookId1", "Book1", author1, genre1),
                new Book("bookId2", "Book2", author2, genre2));

        var expectedBookDtoList = allBooks.stream()
                .map(bookMapper::bookToBookDto)
                .toList();

        given(bookRepository.findAll()).willReturn(Flux.fromIterable(allBooks));

        var result = webClient
                .get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(allBooks.size())
                .returnResult().getResponseBody();

        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedBookDtoList);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findById_givenCorrectId_shouldReturnBookReturnedByRepository() {
        var expectedBookDto = bookMapper.bookToBookDto(BOOK);

        given(bookRepository.findById(BOOK.getId())).willReturn(Mono.just(BOOK));

        var result = webClient
                .get()
                .uri("/api/books/" + BOOK.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult().getResponseBody();

        assertThat(result).isEqualTo(expectedBookDto);
        verify(bookRepository, times(1)).findById(BOOK.getId());
    }

    @Test
    void findById_givenNonExistentBookId_shouldReturnNotFoundAndEmptyBody() {
        given(bookRepository.findById(anyString())).willReturn(Mono.empty());
        webClient
                .get()
                .uri("/api/books/" + NON_EXISTENT_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
        verify(bookRepository, times(1)).findById(NON_EXISTENT_ID);
    }

    @Test
    void addBook_givenCorrectRequest_shouldReturnBookSavedByRepository() {
        CreateBookRequest request = new CreateBookRequest(TITLE, AUTHOR_ID, GENRE_ID);
        var expectedBookDto = bookMapper.bookToBookDto(BOOK);

        given(authorRepository.findById(anyString())).willReturn(Mono.just(AUTHOR));
        given(genreRepository.findById(anyString())).willReturn(Mono.just(GENRE));
        given(bookRepository.save(any())).willReturn(Mono.just(BOOK));

        var result = webClient
                .post()
                .uri("/api/books")
                .body(Mono.just(request), CreateBookRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult().getResponseBody();

        assertThat(result).isEqualTo(expectedBookDto);
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(genreRepository, times(1)).findById(GENRE_ID);
        verify(bookRepository, times(1)).save(
                argThat(b -> Objects.equals(b.getTitle(), TITLE)
                        && Objects.equals(b.getGenre(), GENRE)
                        && Objects.equals(b.getAuthor(), AUTHOR))
        );
    }

    @Test
    void updateBook_givenCorrectRequest_shouldReturnUpdatedBookFromRepository() {

        UpdateBookRequest request = new UpdateBookRequest(BOOK_ID, TITLE, AUTHOR_ID, GENRE_ID);
        var expectedBookDto = bookMapper.bookToBookDto(BOOK);

        given(bookRepository.existsById(BOOK_ID)).willReturn(Mono.just(true));
        given(authorRepository.findById(anyString())).willReturn(Mono.just(AUTHOR));
        given(genreRepository.findById(anyString())).willReturn(Mono.just(GENRE));
        given(bookRepository.save(any())).willReturn(Mono.just(BOOK));

        var result = webClient
                .put()
                .uri("/api/books/" + BOOK_ID)
                .body(Mono.just(request), UpdateBookRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult().getResponseBody();

        assertThat(result).isEqualTo(expectedBookDto);
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(genreRepository, times(1)).findById(GENRE_ID);
        verify(bookRepository, times(1)).save(
                argThat(b -> Objects.equals(b.getId(), BOOK_ID)
                        && Objects.equals(b.getTitle(), TITLE)
                        && Objects.equals(b.getGenre(), GENRE)
                        && Objects.equals(b.getAuthor(), AUTHOR))
        );
    }

    @Test
    void deleteById_shouldDeleteBookAndBookCommentsByIdAndThenReturnOk() {
        given(bookRepository.deleteById(BOOK_ID)).willReturn(Mono.empty());
        given(commentRepository.deleteByBookId(BOOK_ID)).willReturn(Mono.empty());

        webClient.delete()
                .uri("/api/books/" + BOOK_ID)
                .exchange()
                .expectStatus()
                .isOk();

        verify(bookRepository, times(1)).deleteById(BOOK_ID);
        verify(commentRepository, times(1)).deleteByBookId(BOOK_ID);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'', authorId, genreId, 1",
            "title, null, genreId, 1",
            "title, authorId, null, 1",
            "'', '', '', 3",
            "'', null, null, 3"
    }, nullValues = "null")
    void addBook_givenInvalidRequest_shouldPassExceptionToHandlerReturningJsonWithErrors(
            String requestTitle, String requestAuthorId, String requestGenreId, int errorsCount) {
        CreateBookRequest request = new CreateBookRequest(requestTitle, requestAuthorId, requestGenreId);

        webClient
                .post()
                .uri("/api/books")
                .body(Mono.just(request), CreateBookRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors").value(hasSize(errorsCount));
    }

    @Test
    void updateBook_givenInvalidRequest_shouldPassExceptionToHandlerReturningJsonWithErrors() {
        var request = new UpdateBookRequest(BOOK_ID, "", AUTHOR_ID, GENRE_ID);
        var expectedErrorsCount = 1;
        webClient
                .put()
                .uri("/api/books/" + request.id())
                .body(Mono.just(request), UpdateBookRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors").value(hasSize(expectedErrorsCount));
    }

    @ParameterizedTest
    @MethodSource("provideErrorUpdateData")
    void updateBook_shouldReturnExpectedErrorMessageAndHttpStatusWhenAnyEntityDoesNotExist(
            UpdateBookRequest request,
            Mono<Boolean> existsByIdResult, Mono<Author> authorResult, Mono<Genre> genreResult,
            String expectedMessage
    ) {
        given(bookRepository.existsById(anyString())).willReturn(existsByIdResult);
        given(authorRepository.findById(anyString())).willReturn(authorResult);
        given(genreRepository.findById(anyString())).willReturn(genreResult);

        webClient
                .put()
                .uri("/api/books/" + request.id())
                .body(Mono.just(request), UpdateBookRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors").value(hasSize(1))
                .jsonPath("$.errors[0]").isEqualTo(expectedMessage);
    }

    private static Stream<Arguments> provideErrorUpdateData() {
        var request = new UpdateBookRequest(BOOK_ID, TITLE, AUTHOR_ID, GENRE_ID);
        return Stream.of(
                Arguments.of(request, Mono.just(false), Mono.just(AUTHOR), Mono.just(GENRE),
                        "The book does not exist: bookId=" + BOOK_ID),
                Arguments.of(request, Mono.just(true), Mono.empty(), Mono.just(GENRE),
                        "The author does not exist: authorId=" + AUTHOR_ID),
                Arguments.of(request, Mono.just(true), Mono.just(AUTHOR), Mono.empty(),
                        "The genre does not exist: genreId=" + GENRE_ID)
        );
    }

    @ParameterizedTest
    @MethodSource("provideErrorCreateData")
    void addBook_shouldReturnExpectedErrorMessageAndHttpStatusWhenAnyEntityDoesNotExist(
            CreateBookRequest request,
            Mono<Author> authorResult, Mono<Genre> genreResult,
            String expectedMessage
    ) {
        given(authorRepository.findById(anyString())).willReturn(authorResult);
        given(genreRepository.findById(anyString())).willReturn(genreResult);

        webClient
                .post()
                .uri("/api/books")
                .body(Mono.just(request), CreateBookRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors").value(hasSize(1))
                .jsonPath("$.errors[0]").isEqualTo(expectedMessage);
    }

    private static Stream<Arguments> provideErrorCreateData() {
        var request = new CreateBookRequest(TITLE, AUTHOR_ID, GENRE_ID);

        return Stream.of(
                Arguments.of(request, Mono.empty(), Mono.just(GENRE),
                        "The author does not exist: authorId=" + AUTHOR_ID),
                Arguments.of(request, Mono.just(AUTHOR), Mono.empty(),
                        "The genre does not exist: genreId=" + GENRE_ID)
        );
    }
}