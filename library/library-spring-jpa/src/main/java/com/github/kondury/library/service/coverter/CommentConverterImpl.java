package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentConverterImpl implements CommentConverter {
    @Override
    public String convert(Comment comment) {
        return "Comment(id=%s, bookId=%s, text=\"%s\")"
                .formatted(comment.getId(), comment.getBook().getId(), comment.getText());
    }
}
