package com.github.kondury.library.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookPageController.class)
class BookPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void listBooks_shouldReturnBookListPage() throws Exception {
        mvc.perform(get("/books/list"))
                .andExpectAll(
                        view().name("books/bookList"),
                        status().isOk()
                );
    }

    @Test
    void newBook_shouldReturnNewBookPage() throws Exception {
        mvc.perform(get("/books/new"))
                .andExpectAll(
                        view().name("books/bookNew"),
                        status().isOk()
                );
    }

    @Test
    void editBook_shouldReturnEditBookPageWithExpectedModelAttributes() throws Exception {
        mvc.perform(get("/books/1/edit"))
                .andExpectAll(
                        view().name("books/bookUpdate"),
                        model().attribute("bookId", 1L),
                        status().isOk()
                );
    }
}