package com.github.kondury.library.rest;

import com.github.kondury.library.rest.dto.CommentDto;
import com.github.kondury.library.rest.mapper.CommentMapper;
import com.github.kondury.library.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @GetMapping("/api/comments")
    public Flux<CommentDto> findByBookId(@RequestParam("bookId") String bookId) {
        return commentRepository.findCommentsByBookId(bookId)
                .map(commentMapper::commentToCommentDto);
    }

    @GetMapping("/api/comments/{id}")
    public Mono<ResponseEntity<CommentDto>> findById(@PathVariable String id) {
        return commentRepository.findById(id)
                .map(commentMapper::commentToCommentDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/comments/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return commentRepository.deleteById(id);
    }

}
