package com.github.kondury.quiz.service.manager;

import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableAnswer;
import com.github.kondury.quiz.io.validator.VerifiableAnswerValidator;
import com.github.kondury.quiz.utils.AbstractManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class VerifiableAnswerValidatorManagerImpl
        extends AbstractManager<QuestionType, VerifiableAnswerValidator<? extends VerifiableAnswer>>
        implements VerifiableAnswerValidatorManager {
    public VerifiableAnswerValidatorManagerImpl(List<VerifiableAnswerValidator<? extends VerifiableAnswer>> validators) {
        super(validators.stream()
                .collect(Collectors.toMap(VerifiableAnswerValidator::getType, Function.identity())));
    }
}
