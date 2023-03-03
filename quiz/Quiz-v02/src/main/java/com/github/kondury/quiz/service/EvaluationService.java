package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.Student;
import com.github.kondury.quiz.model.QuizResult;

public interface EvaluationService {
    QuizResult getResults(Student student);
}
