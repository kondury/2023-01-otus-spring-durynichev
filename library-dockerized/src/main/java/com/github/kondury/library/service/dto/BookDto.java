package com.github.kondury.library.service.dto;

public record BookDto(Long id, String title, AuthorDto author, GenreDto genre) {
}
