package com.github.kondury.library.domain;

import java.util.Objects;

public record Author(long id, String name) {

    public Author(long id, String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

}
