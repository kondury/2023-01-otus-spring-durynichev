package com.github.kondury.library.rest;


import com.github.kondury.library.rest.dto.AuthorDto;
import com.github.kondury.library.rest.mapper.AuthorMapper;
import com.github.kondury.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @GetMapping("/api/authors")
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .map(authorMapper::authorToAuthorDto);
    }
}
