package com.github.kondury.library.service;

import com.github.kondury.library.service.dto.BookDto;
import com.github.kondury.library.service.dto.CreateBookRequest;
import com.github.kondury.library.service.dto.UpdateBookRequest;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookDto insert(CreateBookRequest request);

    BookDto update(UpdateBookRequest request);

    List<BookDto> findAll();

    Optional<BookDto> findById(long id);

    void deleteById(long id);

    long count();


}
