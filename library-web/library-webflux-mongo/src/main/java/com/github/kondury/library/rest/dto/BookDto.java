package com.github.kondury.library.rest.dto;

public record BookDto(String id, String title, AuthorDto author, GenreDto genre) {
}
