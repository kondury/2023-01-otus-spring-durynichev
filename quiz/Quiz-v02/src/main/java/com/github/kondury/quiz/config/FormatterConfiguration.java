package com.github.kondury.quiz.config;

import com.github.kondury.quiz.domain.*;
import com.github.kondury.quiz.io.formatter.*;
import com.github.kondury.quiz.service.manager.AnswerFormatterManager;
import com.github.kondury.quiz.utils.Manager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;

@Configuration
public class FormatterConfiguration {

    @Bean
    public OutputFormatter<List<Answer>> choiceAnswerFormatter() {
        return new AttemptModeChoiceAnswerFormatter();
    }

    @Bean
    public OutputFormatter<List<Answer>> textAnswerFormatter() {
        return new AttemptModeTextAnswerFormatter();
    }

    @Bean
    public Manager<QuestionType, OutputFormatter<List<Answer>>> answerFactoryManager(
            @Qualifier("choiceAnswerFormatter") OutputFormatter<List<Answer>> choiceAnswerFormatter,
            @Qualifier("textAnswerFormatter") OutputFormatter<List<Answer>> textAnswerFormatter
    ) {
        var formatters = new HashMap<QuestionType, OutputFormatter<List<Answer>>>();
        formatters.put(QuestionType.Choice, choiceAnswerFormatter);
        formatters.put(QuestionType.Text, textAnswerFormatter);
        return new AnswerFormatterManager(formatters);
    }

    @Bean
    public OutputFormatter<Question> questionFormatter(
            final Manager<QuestionType, OutputFormatter<List<Answer>>> answerFactoryManager,
            @Value("${quiz.output.question.formatter.header}:") String header,
            @Value("${quiz.output.question.formatter.footer}:") String footer,
            @Value("${quiz.output.question.formatter.numerate}:false") boolean isSequential
    ) {
        return new EnhancedQuestionFormatter(
                new StandardQuestionFormatter(answerFactoryManager),
                header,
                footer,
                isSequential
        );
    }

}
