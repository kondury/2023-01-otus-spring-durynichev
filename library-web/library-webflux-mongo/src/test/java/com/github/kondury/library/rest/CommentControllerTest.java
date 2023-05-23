package com.github.kondury.library.rest;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.rest.dto.CommentDto;
import com.github.kondury.library.rest.mapper.CommentMapper;
import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.rest.mapper.CommentMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(CommentMapperImpl.class)
@WebFluxTest(CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getCommentsByBookId_shouldReturnCommentListReturnedByService() {

        var paramBookId = "bookId";

        var author = new Author("authorId", "Author");
        var genre = new Genre("genreId", "Genre");
        var book = new Book(paramBookId, "title1", author, genre);
        var comments = List.of(new Comment("comment 1", book), new Comment("comment 2", book));
        var expectedCommentDtoList = comments.stream()
                .map(commentMapper::commentToCommentDto)
                .toList();

        given(commentRepository.findCommentsByBookId(paramBookId)).willReturn(Flux.fromIterable(comments));

        var result = webClient
                .get()
                .uri("/api/comments?bookId=" + paramBookId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(comments.size())
                .returnResult().getResponseBody();

        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedCommentDtoList);
        verify(commentRepository, times(1)).findCommentsByBookId(paramBookId);
    }
}