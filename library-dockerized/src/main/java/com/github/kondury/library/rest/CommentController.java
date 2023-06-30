package com.github.kondury.library.rest;

import com.github.kondury.library.service.dto.CommentDto;
import com.github.kondury.library.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/comments")
    public List<CommentDto> getCommentsByBookId(@RequestParam("bookId") Long id) {
        return commentService.findByBookId(id);
    }
}
