package com.github.kondury.quiz.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "quiz")
public class QuizProperties {
    private final Locale locale;
    private final int evaluationMinPercentageToPass;
    private final String csvFilename;
    private final int numberOfQuestions;
}