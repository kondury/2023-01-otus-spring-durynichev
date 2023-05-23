package com.github.kondury.library.rest;

import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.repository.GenreRepository;
import com.github.kondury.library.rest.dto.GenreDto;
import com.github.kondury.library.rest.mapper.GenreMapper;
import com.github.kondury.library.rest.mapper.GenreMapperImpl;
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

@Import(GenreMapperImpl.class)
@WebFluxTest(controllers = {GenreController.class})
class GenreControllerTest {

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    void findAll_shouldReturnGenresReturnedByRepository() {
        var allGenres = List.of(
                new Genre("genreId1", "Genre 1"),
                new Genre("genreId2", "Genre 2")
        );
        var expectedGenreDtoList = allGenres.stream().map(genreMapper::genreToGenreDto).toList();

        given(genreRepository.findAll()).willReturn(Flux.fromIterable(allGenres));

        var result = webClient
                .get()
                .uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(allGenres.size())
                .returnResult().getResponseBody();

        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedGenreDtoList);
        verify(genreRepository, times(1)).findAll();
    }

}