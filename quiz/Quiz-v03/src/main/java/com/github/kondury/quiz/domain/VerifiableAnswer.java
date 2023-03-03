package com.github.kondury.quiz.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class VerifiableAnswer implements QuestionTyped {

    protected final QuestionType questionType;

    @Override
    public QuestionType getType() {
        return questionType;
    }
}
