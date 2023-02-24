package com.github.kondury.quiz.service.manager;


import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableAnswer;
import com.github.kondury.quiz.domain.VerifiableAnswerFactory;
import com.github.kondury.quiz.utils.AbstractManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class VerifiableAnswerFactoryManagerImpl
        extends AbstractManager<QuestionType, VerifiableAnswerFactory<? extends VerifiableAnswer>>
        implements VerifableAnswerFactoryManager
{
    public VerifiableAnswerFactoryManagerImpl(List<VerifiableAnswerFactory<? extends VerifiableAnswer>> factories) {
        super(factories.stream()
                .collect(Collectors.toMap(VerifiableAnswerFactory::getType, Function.identity()))
        );
    }
}
