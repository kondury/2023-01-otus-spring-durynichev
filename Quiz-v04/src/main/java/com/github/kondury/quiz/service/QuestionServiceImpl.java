package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.AttemptUnit;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.io.InputService;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.io.formatter.QuestionFormatter;
import com.github.kondury.quiz.service.manager.VerifableAnswerFactoryManager;
import com.github.kondury.quiz.service.manager.VerifiableAnswerValidatorManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final OutputService outputService;
    private final InputService inputService;
    private final QuestionFormatter formatter;
    private final VerifableAnswerFactoryManager answerFactoryManager;
    private final VerifiableAnswerValidatorManager answerValidatorManager;

    @Override
    public AttemptUnit ask(final Question question) {
        final String questionStr = formatter.format(question);
        while (true) {
            outputService.output(questionStr);
            final var input = inputService.read();
            final var validator = answerValidatorManager.get(question.type());
            final var validationResult = validator.validate(input, question);
            if (!validationResult.isValid()) {
                outputService.output(validationResult.message());
                continue;
            }
            final var factory = answerFactoryManager.get(question.type());
            final var studentAnswer = factory.createFrom(input);
            return new AttemptUnit(question, studentAnswer);
        }
    }

}
