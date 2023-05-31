package com.github.kondury.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kondury.library.service.BookService;
import com.github.kondury.library.service.EntityDoesNotExistException;
import com.github.kondury.library.service.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final String ERROR_MESSAGE = "Test error message";
    private final BookDto expectedBook =  new BookDto(1L, "Book",
        new AuthorDto(1L, "Author"), new GenreDto(1L, "Genre"));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    @WithMockUser
    @Test
    void findAllBooks_shouldReturnBooksReturnedByService() throws Exception {
        var author1 = new AuthorDto(1L, "Author1");
        var author2 = new AuthorDto(2L, "Author2");
        var genre1 = new GenreDto(1L, "Genre1");
        var genre2 = new GenreDto(2L, "Genre2");
        var books = List.of(
                new BookDto(1L, "Book1", author1, genre1),
                new BookDto(2L, "Book2", author2, genre2));

        given(bookService.findAll()).willReturn(books);
        mvc.perform(get("/api/books"))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(books)));
    }

    @WithMockUser
    @Test
    void findBookById_givenCorrectId_shouldReturnBookDtoJsonFromServiceReturnValue() throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.of(expectedBook));
        mvc.perform(get("/api/books/1"))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(expectedBook)));
    }

    @WithMockUser
    @Test
    void findBookById_givenNonExistentBookId_shouldReturnNull() throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.empty());
        mvc.perform(get("/api/books/1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").doesNotExist());
    }

    @WithMockUser
    @Test
    void addBook_givenCorrectRequest_shouldCallInsertAndReturnBookDtoFromService() throws Exception {
        CreateBookRequest request = new CreateBookRequest("title", 1L, 1L);
        var expectedBook =  new BookDto(1L, "Book",
                new AuthorDto(1L, "Author"),
                new GenreDto(1L, "Genre"));
        given(bookService.insert(request)).willReturn(expectedBook);
        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(expectedBook)));
        verify(bookService, times(1)).insert(request);
    }

    @WithMockUser
    @Test
    void updateBook_givenCorrectRequest_shouldCallUpdateAndReturnBookDtoFromService() throws Exception {
        UpdateBookRequest request = new UpdateBookRequest(1L, "title", 1L, 1L);
        var expectedBook =  new BookDto(1L, "Book",
                new AuthorDto(1L, "Author"),
                new GenreDto(1L, "Genre"));
        given(bookService.update(request)).willReturn(expectedBook);
        mvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(expectedBook)));
        verify(bookService, times(1)).update(request);
    }

    @WithMockUser
    @Test
    void deleteBookById_serviceDeleteIsCalledAndThenRedirect() throws Exception {
        mvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1L);
    }

    @WithMockUser
    @ParameterizedTest
    @CsvSource(value = {
            "'', 1, 1, 1",
            "title, null, 1, 1",
            "title, 1, null, 1",
            "'', null, null, 3"
    }, nullValues = "null")
    void addBook_givenInvalidRequest_shouldPassExceptionToHandlerReturningJsonWithErrors(
            String title, Long authorId, Long genreId, int errorsCount) throws Exception {

        var request = new CreateBookRequest(title, authorId, genreId);
        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath( "$.errors").exists(),
                        jsonPath( "$.errors").isArray(),
                        jsonPath( "$.errors").value(hasSize(errorsCount)));
    }

    @WithMockUser
    @Test
    void updateBook_givenInvalidRequest_shouldPassExceptionToHandlerReturningJsonWithErrors() throws Exception {
        var request = new UpdateBookRequest(1L, "", 1L, 1L);
        var expectedErrorsCount = 1;
        mvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath( "$.errors").exists(),
                        jsonPath( "$.errors").isArray(),
                        jsonPath( "$.errors").value(hasSize(expectedErrorsCount)));
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("provideExceptionTestsData")
    void shouldReturnRespectedErrorMessageAndHttpStatusWhenServiceUpdateThrowsException(
            Throwable ex, HttpStatus resultHttpStatus, String resultReason, String resultMessage) throws Exception {

        UpdateBookRequest request = new UpdateBookRequest(1L, "title", 1L, 1L);
        given(bookService.update(any())).willThrow(ex);
        mvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpectAll(
                        content().string(containsString(resultMessage)),
                        status().reason(resultReason),
                        status().is(resultHttpStatus.value()));
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("provideExceptionTestsData")
    void shouldReturnErrorMessageAndRespectedHttpStatusWhenServiceInsertThrowsException(
            Throwable ex, HttpStatus resultHttpStatus, String resultReason, String resultMessage) throws Exception {

        CreateBookRequest request = new CreateBookRequest("title", 1L, 1L);
        given(bookService.insert(any())).willThrow(ex);
        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpectAll(
                        content().string(containsString(resultMessage)),
                        status().reason(resultReason),
                        status().is(resultHttpStatus.value()));
    }

    public static Stream<Arguments> provideExceptionTestsData() {
        return Stream.of(
                Arguments.of(new EntityDoesNotExistException(ERROR_MESSAGE),
                        HttpStatus.BAD_REQUEST, null, ERROR_MESSAGE),
                Arguments.of(new DataIntegrityViolationException(ERROR_MESSAGE),
                        HttpStatus.CONFLICT, "Data integrity violation", ""),
                Arguments.of(new OptimisticLockingFailureException(ERROR_MESSAGE),
                        HttpStatus.INTERNAL_SERVER_ERROR, null, "Errors")
        );
    }

}