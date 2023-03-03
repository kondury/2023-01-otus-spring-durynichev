package com.github.kondury.quiz.io.validator;


public interface Validator<T> {
    ValidationResult validate(String source);
}
