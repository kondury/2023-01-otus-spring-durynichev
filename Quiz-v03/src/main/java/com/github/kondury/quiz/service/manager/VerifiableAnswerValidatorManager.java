package com.github.kondury.quiz.service.manager;

import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableAnswer;
import com.github.kondury.quiz.io.validator.VerifiableAnswerValidator;
import com.github.kondury.quiz.utils.Manager;


public interface VerifiableAnswerValidatorManager
        extends Manager<QuestionType, VerifiableAnswerValidator<? extends VerifiableAnswer>> {
}
