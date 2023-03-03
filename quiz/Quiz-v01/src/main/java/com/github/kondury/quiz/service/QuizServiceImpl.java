package com.github.kondury.quiz.service;

import com.github.kondury.quiz.dao.QuizDao;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.io.QuestionFormatter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final OutputService outputService;
    private final QuizDao quizDao;
    private final QuestionFormatter formatter;

    public void run() {
        final List<Question> questions = quizDao.getQuestions();
        for (final Question question : questions) {
            outputService.output(formatter.format(question));
        }
    }
}
