package com.github.kondury.quiz.domain;

import lombok.Getter;


public class VerifiableTextAnswer extends VerifiableAnswer {

    @Getter
    private final String text;

    public VerifiableTextAnswer(String text) {
        super(QuestionType.Text);
        this.text = text;
    }
}
