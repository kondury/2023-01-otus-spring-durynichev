package com.github.kondury.quiz.service.manager;

import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableAnswer;
import com.github.kondury.quiz.domain.VerifiableAnswerFactory;
import com.github.kondury.quiz.utils.Manager;


public interface VerifableAnswerFactoryManager
        extends Manager<QuestionType, VerifiableAnswerFactory<? extends VerifiableAnswer>> {
}
