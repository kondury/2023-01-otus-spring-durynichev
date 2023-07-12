package com.github.kondury.library.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookRequest(

        @NotBlank(message = "{book-title-should-not-be-blank}")
        String title,
        @NotNull(message = "{book-author-should-not-be-null}")
        Long authorId,
        @NotNull(message = "{book-genre-should-not-be-null}")
        Long genreId
) {
}
