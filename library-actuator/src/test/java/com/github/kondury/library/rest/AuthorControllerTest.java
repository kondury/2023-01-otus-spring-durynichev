package com.github.kondury.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kondury.library.service.dto.AuthorDto;
import com.github.kondury.library.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @WithMockUser
    @Test
    void getAllAuthors_shouldReturnAuthorsReturnedByService() throws Exception {
        var allAuthors = List.of(new AuthorDto(1L, "Author1"),
                new AuthorDto(2L, "Author2"));
        given(authorService.findAll()).willReturn(allAuthors);
        mvc.perform(get("/api/authors")
                        .with(csrf()))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(allAuthors))
                );
    }
}