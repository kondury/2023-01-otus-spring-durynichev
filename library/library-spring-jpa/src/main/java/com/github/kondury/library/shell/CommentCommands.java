package com.github.kondury.library.shell;

import com.github.kondury.library.service.CommentService;
import com.github.kondury.library.service.coverter.CommentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;
    private final CommentConverter commentConverter;

    @ShellMethod(value = "inserts comment into database", key = {"add-comment", "insert-comment"})
    String insertComment(long bookId, String text) {
        return commentService.insert(bookId, text)
                .map(commentConverter::convert)
                .orElse("Not saved. Book is not found: bookId=" + bookId);
    }

    @ShellMethod(value = "returns all comments by book", key = {"find-comments"})
    String findCommentsByBookId(long bookId) {
        var comments = commentService.findByBookId(bookId);
        if (!comments.isEmpty()) {
            return comments.stream()
                    .map(commentConverter::convert)
                    .collect(Collectors.joining("\n"));
        }
        return "Comments weren't found: bookId=" + bookId;
    }

    @ShellMethod(value = "returns comment by id", key = {"find-comment"})
    String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::convert)
                .orElse("Comment is not found: id=" + id);
    }

    @ShellMethod(value = "updates comment properties", key = "update-comment")
    String updateBook(long commentId, long bookId, String text) {
        return commentService.update(commentId, bookId, text)
                .map(commentConverter::convert)
                .orElse("Not saved. Book is not found: bookId=" + bookId);
    }

    @ShellMethod(value = "deletes comment by id", key = {"delete-comment"})
    String deleteCommentById(long id) {
        return commentService.deleteById(id) ? "Successfully deleted." : "Comment is not found: id=" + id;
    }

}

