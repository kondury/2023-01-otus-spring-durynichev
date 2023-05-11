package com.github.kondury.library.dto;

import jakarta.validation.constraints.NotBlank;


public record UpdateBookRequest(
        Long id,
        @NotBlank(message = "{book-title-should-not-be-blank}")
        String title,
        Long authorId,
        Long genreId
) { }
