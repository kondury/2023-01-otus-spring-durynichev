package com.github.kondury.library.security;

import com.github.kondury.library.page.BookPageController;
import com.github.kondury.library.page.CommentPageController;
import com.github.kondury.library.rest.AuthorController;
import com.github.kondury.library.rest.BookController;
import com.github.kondury.library.rest.CommentController;
import com.github.kondury.library.rest.GenreController;
import com.github.kondury.library.service.AuthorService;
import com.github.kondury.library.service.BookService;
import com.github.kondury.library.service.CommentService;
import com.github.kondury.library.service.GenreService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookController.class, AuthorController.class, GenreController.class, CommentController.class,
        BookPageController.class, CommentPageController.class})
@Import(WebSecurityConfig.class)
public class ControllersSecurityTests {

    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mvc;

    private static Stream<Arguments> provideSecuredEndpointData() {
        var getEndpoints = Stream.of(
                        "/api/authors", "/api/genres", "/api/books", "/api/books/1",
                        "/api/comments?bookId=1",
                        "/book/new", "/book/list", "/book/1/edit",
                        "/comments/list")
                .map(path -> Arguments.of(path, HttpMethod.GET));
        var modifyingEndpoints = Stream.of(
                Arguments.of("/api/books", HttpMethod.POST),
                Arguments.of("/api/books/2", HttpMethod.PUT),
                Arguments.of("/api/books/2", HttpMethod.DELETE)
        );
        return Stream.concat(getEndpoints, modifyingEndpoints);
    }

    private static Stream<Arguments> provideWrongAuthorizationData() {
        var allAuthorities = Set.of("READ", "CREATE", "UPDATE", "DELETE");
        var readArguments = Stream.of(
                        "/", "/book/new", "/book/list", "/book/1/edit", "/comments/list",
                        "/api/authors", "/api/genres", "/api/books", "/api/books/1", "/api/comments?bookId=1"
                )
                .map(path -> Arguments.of(path, HttpMethod.GET, excludeAuthorityByName(allAuthorities, "READ")));
        var createArguments = Stream.of(Arguments.of("/api/books", HttpMethod.POST,
                excludeAuthorityByName(allAuthorities, "CREATE")));
        var updateArguments = Stream.of(Arguments.of("/api/books/2", HttpMethod.PUT,
                excludeAuthorityByName(allAuthorities, "UPDATE")));
        var deleteArguments = Stream.of(Arguments.of("/api/books/2", HttpMethod.DELETE,
                excludeAuthorityByName(allAuthorities, "DELETE")));
        return Stream.of(readArguments, createArguments, updateArguments, deleteArguments)
                .flatMap(s -> s);
    }

    private static String[] excludeAuthorityByName(Set<String> authorities, String exclude) {
        return authorities.stream()
                .filter(auth -> !auth.equals(exclude))
                .toArray(String[]::new);
    }

    @ParameterizedTest
    @MethodSource("provideSecuredEndpointData")
    void securedEndpointsForbidUnauthenticatedAccessAndReturn401(String path, HttpMethod httpMethod) throws Exception {
        mvc.perform(request(httpMethod, path).with(csrf()))
                .andExpectAll(
                        status().isUnauthorized(),
                        unauthenticated());
    }

    @ParameterizedTest
    @MethodSource("provideWrongAuthorizationData")
    void securedEndpointsForbidUnauthorizedAccessAndReturn403(String path, HttpMethod httpMethod, String[] roles) throws Exception {
        mvc.perform(request(httpMethod, path).with(csrf()).with(user("testUser").roles(roles)))
                .andExpectAll(
                        status().isForbidden(),
                        authenticated());
    }

}
