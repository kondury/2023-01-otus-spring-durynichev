package com.github.kondury.library.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateBookRequest (

    @NotBlank(message = "{book-title-should-not-be-blank}")
    String title,
    @NotEmpty(message = "{book-author-should-not-be-null}")
    String authorId,
    @NotEmpty(message = "{book-genre-should-not-be-null}")
    String genreId
) { }
