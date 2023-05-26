package com.github.kondury.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kondury.library.dto.GenreDto;
import com.github.kondury.library.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @Test
    void getAllGenres_shouldReturnGenresReturnedByService() throws Exception {
        var allGenres = List.of(new GenreDto(1L, "Genre1"),
                new GenreDto(2L, "Genre2"));
        given(genreService.findAll()).willReturn(allGenres);
        mvc.perform(get("/api/genres"))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(allGenres))
                );
    }
}




