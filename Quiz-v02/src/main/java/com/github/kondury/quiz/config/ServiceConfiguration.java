package com.github.kondury.quiz.config;

import com.github.kondury.quiz.dao.AttemptDao;
import com.github.kondury.quiz.dao.QuizDao;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.StudentFactory;
import com.github.kondury.quiz.io.InputService;
import com.github.kondury.quiz.io.formatter.OutputFormatter;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.service.*;
import com.github.kondury.quiz.service.manager.VerifableAnswerFactoryManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public AcquaintanceService acquaintanceService(
            final OutputService outputService,
            final InputService inputService,
            final StudentFactory studentFactory
    ) {
        return new AcquaintanceServiceImpl(outputService, inputService, studentFactory);
    }

    @Bean
    QuestionService questionService(
            final OutputService outputService,
            final InputService inputService,
            final OutputFormatter<Question> formatter,
            final VerifableAnswerFactoryManager verifableAnswerFactoryManager
    ) {
        return new QuestionServiceImpl(outputService, inputService, formatter, verifableAnswerFactoryManager);
    }

    @Bean
    EvaluationService evaluationService(
            @Value("${quiz.evaluation.min-percentage-to-pass}:80") final int minPercentageToPass,
            final AttemptDao attemptDao
    ) {
        return new EvaluationServiceImpl(minPercentageToPass, attemptDao);
    }

    @Bean
    public QuizService quizService(
            final OutputService outputService,
            final AcquaintanceService acquaintanceService,
            final QuizDao quizDao,
            final AttemptDao attemptDao,
            final QuestionService questionService,
            final EvaluationService evaluationService,
            @Value("${quiz.csv.number-of-questions}:5") final int numberOfQuestions
    ) {
        return new QuizServiceImpl(
                outputService,
                acquaintanceService,
                quizDao,
                attemptDao,
                questionService,
                evaluationService,
                numberOfQuestions);
    }
}
