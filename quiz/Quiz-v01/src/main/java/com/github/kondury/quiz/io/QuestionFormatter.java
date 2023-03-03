package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.Question;

@FunctionalInterface
public interface QuestionFormatter {

    String format(final Question question);

}
