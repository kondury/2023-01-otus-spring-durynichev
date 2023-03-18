package com.github.kondury.library.domain;

import java.util.Objects;

public record Genre(
        long id,
        String name) {

    public Genre(long id, String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

}