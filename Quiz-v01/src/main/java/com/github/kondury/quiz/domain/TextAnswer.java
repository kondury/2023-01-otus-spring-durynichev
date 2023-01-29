package com.github.kondury.quiz.domain;

public record TextAnswer(String answer) implements Answer {

    @Override
    public boolean check(Answer other) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
