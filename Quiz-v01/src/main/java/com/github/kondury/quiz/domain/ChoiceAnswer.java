package com.github.kondury.quiz.domain;

import java.util.Collections;
import java.util.Map;

public record ChoiceAnswer(Map<String, Boolean> choices) implements Answer {
    public ChoiceAnswer(Map<String, Boolean> choices) {
        this.choices = Collections.unmodifiableMap(choices);
    }

    @Override
    public boolean check(final Answer other) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
