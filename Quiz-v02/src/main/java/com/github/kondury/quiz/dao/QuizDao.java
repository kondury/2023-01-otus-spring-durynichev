package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.Question;

import java.util.List;

public interface QuizDao {
    List<Question> getQuestions();
}
