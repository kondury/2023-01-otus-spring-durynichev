package com.github.kondury.library.domain;

import java.util.Objects;

public record Book(long id, String title, Author author, Genre genre) {

    public Book(long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = Objects.requireNonNull(title);
        this.author = Objects.requireNonNull(author);
        this.genre = Objects.requireNonNull(genre);
    }

    public Book(String title, Author author, Genre genre) {
        this(0, title, author, genre);
    }
}
