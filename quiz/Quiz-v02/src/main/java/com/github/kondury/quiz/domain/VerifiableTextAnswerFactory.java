package com.github.kondury.quiz.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VerifiableTextAnswerFactory implements VerifiableAnswerFactory<VerifiableTextAnswer> {
    @Override
    public VerifiableTextAnswer createFrom(String source) {
        if (source == null || source.isBlank()) {
            throw new EntityCreateException("Illegal argument value: source='%s'".formatted(source));
        }
        return new VerifiableTextAnswer(source);
    }
}
