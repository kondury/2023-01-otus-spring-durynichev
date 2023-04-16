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

    @ShellMethod(value = "inserts a comment into the database", key = {"add-comment", "insert-comment"})
    String insertComment(String bookId, String text) {
        return commentService.insert(bookId, text)
                .map(commentConverter::convert)
                .orElse("The comment was not inserted. " +
                        "Check that the database contains a book having such an id: bookId=" + bookId);
    }

    @ShellMethod(value = "returns all comments by book", key = {"find-comments"})
    String findCommentsByBookId(String bookId) {
        var comments = commentService.findByBookId(bookId);
        if (!comments.isEmpty()) {
            return comments.stream()
                    .map(commentConverter::convert)
                    .collect(Collectors.joining("\n"));
        }
        return "Comments weren't found: bookId=" + bookId;
    }

    @ShellMethod(value = "returns a comment by id", key = {"find-comment"})
    String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::convert)
                .orElse("A comment is not found: id=" + id);
    }

    @ShellMethod(value = "updates comment properties", key = "update-comment")
    String updateComment(String commentId, String bookId, String text) {
        return commentService.update(commentId, bookId, text)
                .map(commentConverter::convert)
                .orElse("The comment was not updated. Check that the database contains a book and" +
                        " a comment with their respected ids: commentId=" + commentId + ", bookId=" + bookId);
    }

    @ShellMethod(value = "deletes comment by id", key = {"delete-comment"})
    String deleteCommentById(String id) {
        commentService.deleteById(id);
        return "The delete command is executed.";
    }

}

