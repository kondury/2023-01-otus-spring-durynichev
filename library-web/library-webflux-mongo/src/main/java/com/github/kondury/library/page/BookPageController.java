package com.github.kondury.library.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class BookPageController {

    @GetMapping({"/", "/books/list"})
    public String listBooks() {
        return "books/bookList";
    }

    @GetMapping("/books/new")
    public String newBook() {
        return "books/bookNew";
    }

    @GetMapping("/books/{id}/edit")
    public String editBook(@PathVariable String id, Model model) {
        model.addAttribute("bookId", id);
        return "books/bookUpdate";
    }
}