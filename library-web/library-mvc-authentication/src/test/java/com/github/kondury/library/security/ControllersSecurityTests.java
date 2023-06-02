package com.github.kondury.library.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllersSecurityTests {

    @Autowired
    private MockMvc mvc;

    private static Stream<Arguments> provideModifyingEndpointsData() {
        return Stream.of(
                Arguments.of("/api/books", HttpMethod.POST),
                Arguments.of("/api/books/2", HttpMethod.PUT),
                Arguments.of("/api/books/2", HttpMethod.DELETE)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "/api/books", "/api/books/1",
            "/api/authors", "/api/genres",
            "/api/comments?bookId=1",
            "/book/list", "/book/new", "/book/1/edit",
            "/comments/list"
    })
    void getEndpointsAreSecured(String path) throws Exception {
        mvc.perform(get(path))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(unauthenticated());
    }

    @ParameterizedTest
    @MethodSource("provideModifyingEndpointsData")
    void modifyingEndpointsAreSecured(String path, HttpMethod httpMethod) throws Exception {
        mvc.perform(request(httpMethod, path))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated());
    }
}
