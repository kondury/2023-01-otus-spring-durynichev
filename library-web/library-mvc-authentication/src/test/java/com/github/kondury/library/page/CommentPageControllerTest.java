package com.github.kondury.library.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentPageController.class)
class CommentPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser
    @Test
    void listCommentsByBookId_shouldReturnCommentsListPage() throws Exception {
        mvc.perform(get("/comments/list").param("bookId", "1")
                        .with(csrf()))
                .andExpectAll(
                        view().name("comments/commentsList"),
                        model().attribute("bookId", 1L),
                        status().isOk()
                );
    }
}


