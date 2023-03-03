package com.github.kondury.quiz.service;

import com.github.kondury.quiz.config.QuizProperties;
import com.github.kondury.quiz.dao.AttemptDao;
import com.github.kondury.quiz.dao.QuizDao;
import com.github.kondury.quiz.domain.AttemptUnit;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.Student;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.model.QuizResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizProperties properties;
    private final OutputService outputService;
    private final AcquaintanceService acquaintanceService;
    private final QuizDao quizDao;
    private final AttemptDao attemptDao;
    private final QuestionService questionService;
    private final EvaluationService evaluationService;

    public void run() {
        final List<Question> questions = quizDao.getQuestions();

        int numberOfQuestions = properties.getNumberOfQuestions();
        if (questions.size() != numberOfQuestions) {
            throw new IllegalStateException("Total number of questions in the source is " +
                    "%s and required is %s".formatted(questions.size(), numberOfQuestions));
        }

        final Student student = acquaintanceService.getAcquaintance();
        for (final Question question : questions) {
            AttemptUnit attemptUnit = questionService.ask(question);
            attemptDao.register(student, attemptUnit);
        }
        QuizResult result = evaluationService.getResults(student);
        outputService.output(result.message());
    }

}
