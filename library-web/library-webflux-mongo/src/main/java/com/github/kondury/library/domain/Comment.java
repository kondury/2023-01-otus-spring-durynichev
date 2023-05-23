package com.github.kondury.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("comments")
public class Comment {
    @Id
    private String id;
    private String text;
    private String bookId;
    @ReadOnlyProperty
    private Book book;

    public Comment(String text, Book book) {
        this.text = text;
        this.book = book;
        this.bookId = book.getId();
    }

}
