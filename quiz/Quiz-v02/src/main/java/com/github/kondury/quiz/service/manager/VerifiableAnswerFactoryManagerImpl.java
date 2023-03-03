package com.github.kondury.quiz.service.manager;


import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableAnswer;
import com.github.kondury.quiz.domain.VerifiableAnswerFactory;
import com.github.kondury.quiz.utils.AbstractManager;

import java.util.Map;

public class VerifiableAnswerFactoryManagerImpl
        extends AbstractManager<QuestionType, VerifiableAnswerFactory<? extends VerifiableAnswer>>
        implements VerifableAnswerFactoryManager
{
    public VerifiableAnswerFactoryManagerImpl(Map<QuestionType, VerifiableAnswerFactory<? extends VerifiableAnswer>> strategies) {
        super(strategies);
    }
}
