package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.QuestionTyped;

import java.util.List;

public interface AnswerFormatter extends OutputFormatter<List<Answer>>, QuestionTyped  {
}
