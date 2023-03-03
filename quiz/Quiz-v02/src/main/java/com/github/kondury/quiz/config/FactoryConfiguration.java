package com.github.kondury.quiz.config;

import com.github.kondury.quiz.domain.*;
import com.github.kondury.quiz.service.manager.VerifiableAnswerFactoryManagerImpl;
import com.github.kondury.quiz.service.manager.VerifableAnswerFactoryManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class FactoryConfiguration {
    @Bean
    public QuestionFactory questionFactory() {
        return new QuestionFactory();
    }

    @Bean
    public StudentFactory studentFactory() {
        return new StudentFactory();
    }

    @Bean
    public VerifiableAnswerFactory<VerifiableTextAnswer> textAnswerFactory() {
        return new VerifiableTextAnswerFactory();
    }

    @Bean VerifiableAnswerFactory<VerifiableChoiceAnswer> choiceAnswerFactory() {
        return new VerifiableChoiceAnswerFactory();
    }

    @Bean
    public VerifableAnswerFactoryManager verifableAnswerFactoryManager() {
        var factories = new HashMap<QuestionType, VerifiableAnswerFactory<? extends VerifiableAnswer>>();
        factories.put(QuestionType.Choice, choiceAnswerFactory());
        factories.put(QuestionType.Text, textAnswerFactory());
        return new VerifiableAnswerFactoryManagerImpl(factories);
    }


}
