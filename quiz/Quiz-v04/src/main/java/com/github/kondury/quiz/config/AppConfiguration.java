package com.github.kondury.quiz.config;

import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.io.formatter.AnswerFormatter;
import com.github.kondury.quiz.io.formatter.EnhancedQuestionFormatter;
import com.github.kondury.quiz.io.formatter.QuestionFormatter;
import com.github.kondury.quiz.io.formatter.StandardQuestionFormatter;
import com.github.kondury.quiz.utils.Manager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppProperties.class})
public class AppConfiguration {

    @Bean
    QuestionFormatter questionFormatter(
            final Manager<QuestionType, AnswerFormatter> answerFactoryManager,
            QuestionFormatterSettingsProvider questionFormatterSettingsProvider
    ) {
        var questionFormatterSettings = questionFormatterSettingsProvider.getQuestionFormatterSettings();
        return new EnhancedQuestionFormatter(
                new StandardQuestionFormatter(answerFactoryManager),
                questionFormatterSettings.isSequential(),
                questionFormatterSettings.getHeader(),
                questionFormatterSettings.getFooter()
        );
    }
}
