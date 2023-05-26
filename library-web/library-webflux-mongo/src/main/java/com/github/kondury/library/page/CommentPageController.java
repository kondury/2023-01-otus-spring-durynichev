package com.github.kondury.library.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class CommentPageController {

    @GetMapping("comments/list")
    public String listCommentsByBookId(@RequestParam("bookId") String id, Model model) {
        model.addAttribute("bookId", id);
        return "comments/commentsList";
    }
}
