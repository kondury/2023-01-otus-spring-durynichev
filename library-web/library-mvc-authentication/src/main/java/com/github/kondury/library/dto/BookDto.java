package com.github.kondury.library.dto;

public record BookDto(Long id, String title, AuthorDto author, GenreDto genre) {
}
