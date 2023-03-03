package com.github.kondury.quiz.domain;

import java.util.List;

public record Question(String text, QuestionType type, List<Answer> answers) {
}
