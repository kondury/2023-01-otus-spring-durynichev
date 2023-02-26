package com.github.kondury.quiz.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application")
public class AppProperties
        implements LocaleProvider, EvaluationSettingsProvider, CsvDaoSettingsProvider,
        QuizServiceSettingsProvider, QuestionFormatterSettingsProvider {
    private final Locale locale;
    private final int minPercentageToPass;
    private final String csvFilename;
    private final int questionsNumber;
    private final QuestionFormatterSettings questionFormatterSettings;

}