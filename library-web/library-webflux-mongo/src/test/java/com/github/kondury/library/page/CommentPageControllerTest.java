package com.github.kondury.library.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = CommentPageController.class)
class CommentPageControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void listCommentsByBookId_shouldReturnCommentsListPage() {
        var result = webClient
                .get()
                .uri("/comments/list?bookId=%s".formatted("mockedBookId"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(result).contains("<title>Comments list</title>");
    }
}