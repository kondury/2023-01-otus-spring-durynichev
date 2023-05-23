package com.github.kondury.library.rest;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.rest.dto.AuthorDto;
import com.github.kondury.library.rest.mapper.AuthorMapper;
import com.github.kondury.library.rest.mapper.AuthorMapperImpl;
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

@Import(AuthorMapperImpl.class)
@WebFluxTest(controllers = {AuthorController.class})
class AuthorControllerTest {

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    void findAll_shouldReturnAuthorsReturnedByRepository() {
        var allAuthors = List.of(
                new Author("authorId1", "Author 1"),
                new Author("authorId2", "Author 2")
        );
        var expectedAuthorDtoList = allAuthors.stream().map(authorMapper::authorToAuthorDto).toList();

        given(authorRepository.findAll()).willReturn(Flux.fromIterable(allAuthors));

        var result = webClient
                .get()
                .uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(allAuthors.size())
                .returnResult().getResponseBody();

        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedAuthorDtoList);
        verify(authorRepository, times(1)).findAll();
    }

}