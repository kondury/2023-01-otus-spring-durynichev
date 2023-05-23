package com.github.kondury.library.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = BookPageController.class)
class BookPageControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void listBooks_shouldReturnBookListPage() {

        var result = webClient
                .get()
                .uri("/books/list")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(result).contains("<title>List of all books</title>");

    }

    @Test
    void newBook_shouldReturnNewBookPage() {
        var result = webClient
                .get()
                .uri("/books/new")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(result).contains("<title>Add book</title>");
    }

    @Test
    void editBook_shouldReturnEditBookPageWithExpectedModelAttributes() {
        var result = webClient
                .get()
                .uri("/books/%s/edit".formatted("mockedBookId"))
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(result).contains("<title>Edit book</title>");
    }
}