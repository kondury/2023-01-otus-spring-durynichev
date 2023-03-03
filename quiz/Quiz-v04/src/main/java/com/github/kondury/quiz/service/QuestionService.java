package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.AttemptUnit;
import com.github.kondury.quiz.domain.Question;


public interface QuestionService {

    AttemptUnit ask(Question question);
}
