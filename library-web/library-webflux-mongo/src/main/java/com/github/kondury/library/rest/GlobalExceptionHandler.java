package com.github.kondury.library.rest;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, List<String>>>> handleValidationErrors(WebExchangeBindException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return Mono.just(
                ResponseEntity
                        .badRequest()
                        .body(getErrorsMap(errors)));
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    public Mono<ResponseEntity<Map<String, List<String>>>> handleNotFound(EntityDoesNotExistException ex) {
        var error = List.of(ex.getMessage());
        return Mono.just(
                ResponseEntity
                        .badRequest()
                        .body(getErrorsMap(error)));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}
