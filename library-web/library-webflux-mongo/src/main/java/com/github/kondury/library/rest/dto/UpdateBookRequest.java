package com.github.kondury.library.rest.dto;

import jakarta.validation.constraints.NotBlank;


public record UpdateBookRequest(
        String id,
        @NotBlank(message = "{book-title-should-not-be-blank}")
        String title,
        String authorId,
        String genreId
) { }
