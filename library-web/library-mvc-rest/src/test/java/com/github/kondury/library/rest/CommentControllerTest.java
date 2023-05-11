package com.github.kondury.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kondury.library.dto.CommentDto;
import com.github.kondury.library.service.CommentService;
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

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getCommentsByBookId_shouldReturnCommentListReturnedByService() throws Exception {
        var comments = List.of(new CommentDto(1L, "Comment1", 1L),
                new CommentDto(2L, "Comment2", 2L));
        given(commentService.findByBookId(1L)).willReturn(comments);
        mvc.perform(get("/api/comments").param("bookId", "1"))
                .andExpectAll(
                        status().isOk(),
                        content().json(mapper.writeValueAsString(comments))
                );
    }
}