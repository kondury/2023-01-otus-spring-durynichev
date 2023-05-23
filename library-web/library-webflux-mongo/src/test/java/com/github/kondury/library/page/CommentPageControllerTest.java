package com.github.kondury.library.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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