package com.github.kondury.quiz.io.validator;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionTyped;
import com.github.kondury.quiz.domain.VerifiableAnswer;


public interface VerifiableAnswerValidator<T extends VerifiableAnswer>
        extends Validator<T>, QuestionTyped {
    ValidationResult validate(String source, Question question);
}
