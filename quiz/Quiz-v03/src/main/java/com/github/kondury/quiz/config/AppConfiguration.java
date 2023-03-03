package com.github.kondury.quiz.config;

import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.io.formatter.AnswerFormatter;
import com.github.kondury.quiz.io.formatter.EnhancedQuestionFormatter;
import com.github.kondury.quiz.io.formatter.QuestionFormatter;
import com.github.kondury.quiz.io.formatter.StandardQuestionFormatter;
import com.github.kondury.quiz.utils.Manager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({QuizProperties.class})
@PropertySource(value = {"${quiz.question-formatter-properties}"}, encoding = "UTF-8")
public class AppConfiguration {

    @Bean
    QuestionFormatter questionFormatter(
            final Manager<QuestionType, AnswerFormatter> answerFactoryManager,
            @Value("${output.question.formatter.header:}") String header,
            @Value("${output.question.formatter.footer:}") String footer,
            @Value("${output.question.formatter.numerate:false}") boolean isSequential
    ) {
        return new EnhancedQuestionFormatter(
                new StandardQuestionFormatter(answerFactoryManager),
                header,
                footer,
                isSequential
        );
    }
}
