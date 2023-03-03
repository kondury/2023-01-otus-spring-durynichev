package com.github.kondury.quiz.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionFormatterSettings {
    private final String header;
    private final String footer;
    private final boolean isSequential;
}
