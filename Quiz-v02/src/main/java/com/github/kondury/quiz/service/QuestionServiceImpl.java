package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.AttemptUnit;
import com.github.kondury.quiz.domain.EntityCreateException;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.io.InputService;
import com.github.kondury.quiz.io.formatter.OutputFormatter;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.service.manager.VerifableAnswerFactoryManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final OutputService outputService;
    private final InputService inputService;
    private final OutputFormatter<Question> formatter;
    private final VerifableAnswerFactoryManager answerFactoryManager;

    @Override
    public AttemptUnit ask(final Question question) {
        while (true) {
            try {
                outputService.output(formatter.format(question));
                var factory = answerFactoryManager.get(question.type());
                var studentAnswer = inputService.readWith(factory);
                return new AttemptUnit(question, studentAnswer);
            } catch (EntityCreateException e) {
                outputService.output(e.getMessage());
            }
        }
    }

}
