package com.github.kondury.quiz.model;

import com.github.kondury.quiz.domain.Student;

public record QuizResult(Student student, boolean passed, String message) {
}
