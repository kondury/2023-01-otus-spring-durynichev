package com.github.kondury.library.controller;

import com.github.kondury.library.dto.*;
import com.github.kondury.library.service.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
class LibraryControllerTest {

    private final List<AuthorDto> allAuthors = List.of(new AuthorDto(1L, "Author1"),
            new AuthorDto(2L, "Author2"));
    private final List<GenreDto> allGenres = List.of(new GenreDto(1L, "Genre1"),
            new GenreDto(2L, "Genre2"));
    private final BookDto bookDto = new BookDto(1L, "The Book", allAuthors.get(0), allGenres.get(0));

    private static final String ERROR_MESSAGE = "Test error message";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private CommentService commentService;

    @Test
    void listBooks_shouldReturnPageContainingExpectedBookDtos() throws Exception {
        List<BookDto> books = List.of(bookDto);
        given(bookService.findAll()).willReturn(books);
        mvc.perform(get("/books/list"))
                .andDo(print())
                .andExpectAll(
                        view().name("books/bookList"),
                        model().attribute("books", books),
                        status().isOk(),
                        content().string(stringContainsInOrder(
                                bookDto.title(), bookDto.author().name(), bookDto.genre().name()))
                );
    }

    @Test
    void newBook_shouldReturnNewBookPageWithExpectedModelAttributes() throws Exception {
        given(authorService.findAll()).willReturn(allAuthors);
        given(genreService.findAll()).willReturn(allGenres);
        mvc.perform(get("/books/new"))
                .andExpectAll(
                        view().name("books/bookNew"),
                        model().attribute("allAuthors", allAuthors),
                        model().attribute("allGenres", allGenres),
                        status().isOk()
                );
    }

    @Test
    void addBook_givenCorrectRequest_shouldCallInsertAndRedirectToMainPage() throws Exception {
        CreateBookRequest request = new CreateBookRequest("title", 1L, 1L);
        mvc.perform(MockMvcRequestBuilders.post("/books/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", request.title())
                        .param("authorId", String.valueOf(request.authorId()))
                        .param("genreId", String.valueOf(request.genreId())))
                .andExpectAll(
                        view().name("redirect:/"),
                        status().is3xxRedirection()
                );
        verify(bookService, times(1)).insert(request);
    }

    @ParameterizedTest
    @CsvSource(value = {"'', 1, 1", "title, null, 1", "title, 1, null"}, nullValues = "null")
    void addBook_shouldReturnToEditPageWhenParametersAreNotValid(String title, Long authorId, Long genreId) throws Exception {
        given(authorService.findAll()).willReturn(allAuthors);
        given(genreService.findAll()).willReturn(allGenres);

        mvc.perform(MockMvcRequestBuilders.post("/books/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("authorId", String.valueOf(authorId))
                        .param("genreId", String.valueOf(genreId)))
                .andExpectAll(
                        view().name("books/bookNew"),
                        model().attribute("allAuthors", allAuthors),
                        model().attribute("allGenres", allGenres),
                        model().attributeHasErrors("newBook"),
                        status().isOk()
                );
    }

    @Test
    void shouldReturnEditBookPageWithExpectedModelAttributes() throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.of(bookDto));
        given(authorService.findAll()).willReturn(allAuthors);
        given(genreService.findAll()).willReturn(allGenres);
        mvc.perform(get("/books/1/edit"))
                .andExpectAll(
                        view().name("books/bookEdit"),
                        model().attribute("allAuthors", allAuthors),
                        model().attribute("allGenres", allGenres),
                        model().attribute("bookDto", bookDto),
                        status().isOk()
                );
    }

    @Test
    void updateBook_givenCorrectRequest_shouldCallUpdateAndRedirectToMainPage() throws Exception {
        UpdateBookRequest request = new UpdateBookRequest(1L, "title", 1L, 1L);
        mvc.perform(MockMvcRequestBuilders.post("/books/1/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(request.id()))
                        .param("title", request.title())
                        .param("authorId", String.valueOf(request.authorId()))
                        .param("genreId", String.valueOf(request.genreId())))
                .andExpectAll(
                        view().name("redirect:/"),
                        status().is3xxRedirection()
                );
        verify(bookService, times(1)).update(request);
    }

    @Test
    void updateBook_shouldReturnToEditPageWhenTitleIsEmpty() throws Exception {
        var authorDto = allAuthors.get(0);
        var genreDto = allGenres.get(0);
        var expectedBootDto = new BookDto(1L, "", authorDto, genreDto);

        given(authorService.findAll()).willReturn(allAuthors);
        given(genreService.findAll()).willReturn(allGenres);
        given(authorService.findById(anyLong())).willReturn(Optional.of(authorDto));
        given(genreService.findById(anyLong())).willReturn(Optional.of(genreDto));

        mvc.perform(MockMvcRequestBuilders.post("/books/1/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(1L))
                        .param("title", "")
                        .param("authorId", String.valueOf(authorDto.id()))
                        .param("genreId", String.valueOf(genreDto.id())))
                .andExpectAll(
                        view().name("books/bookEdit"),
                        model().attribute("allAuthors", allAuthors),
                        model().attribute("allGenres", allGenres),
                        model().attribute("bookDto", expectedBootDto),
                        model().attributeHasErrors("updateBook"),
                        status().isOk()
                );
    }

    @ParameterizedTest
    @CsvSource({"/books/1/edit, 'The book does not exist: bookId=1'",
            "/books/1/comments/list, 'The book does not exist: bookId=1'"})
    void shouldReturnErrorMessageWhenBookDoesNotExist(String url, String expectedMessage) throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.empty());
        mvc.perform(get(url))
                .andExpectAll(
                        content().string(containsString(expectedMessage)),
                        status().isBadRequest()
                );
    }

    @Test
    void listBookComments_shouldReturnPageContainingExpectedBookDtoAndComments() throws Exception {
        var bookId = bookDto.id();
        List<CommentDto> comments = List.of(new CommentDto(1L, "Comment1", bookId),
                new CommentDto(2L, "Comment2", bookId));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));
        given(commentService.findByBookId(bookId)).willReturn(comments);

        mvc.perform(get("/books/1/comments/list"))
                .andDo(print())
                .andExpectAll(
                        view().name("books/bookCommentsList"),
                        model().attribute("bookDto", bookDto),
                        model().attribute("comments", comments),
                        status().isOk(),
                        content().string(stringContainsInOrder(
                                bookDto.title(), bookDto.author().name(), bookDto.genre().name())),
                        content().string(stringContainsInOrder(comments.stream().map(CommentDto::text).toList()))
                );
    }

    @Test
    void deleteBook_serviceDeleteIsCalledAndThenRedirect() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/books/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpectAll(
                        view().name("redirect:/"),
                        status().is3xxRedirection());
        verify(bookService, times(1)).deleteById(1L);
    }

    @ParameterizedTest
    @MethodSource("provideExceptionTestsData")
    void shouldReturnRespectedErrorMessageAndHttpStatusWhenServiceUpdateThrowsException(
            Throwable ex, HttpStatus resultHttpStatus, String resultReason, String resultMessage) throws Exception {

        given(bookService.update(any())).willThrow(ex);
        mvc.perform(MockMvcRequestBuilders.post("/books/1/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(1L))
                        .param("title", "The Book")
                        .param("authorId", String.valueOf(1L))
                        .param("genreId", String.valueOf(1L)))
                .andExpectAll(
                        content().string(containsString(resultMessage)),
                        status().reason(resultReason),
                        status().is(resultHttpStatus.value()));
    }

    @ParameterizedTest
    @MethodSource("provideExceptionTestsData")
    void shouldReturnErrorMessageAndRespectedHttpStatusWhenServiceInsertThrowsException(
            Throwable ex, HttpStatus resultHttpStatus, String resultReason, String resultMessage) throws Exception {

        given(bookService.insert(any())).willThrow(ex);
        mvc.perform(MockMvcRequestBuilders.post("/books/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "The Book")
                        .param("authorId", String.valueOf(1L))
                        .param("genreId", String.valueOf(1L)))
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
                        HttpStatus.INTERNAL_SERVER_ERROR, null, "Database error")
        );
    }

}