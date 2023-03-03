package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;

import java.util.List;

public class AttemptModeTextAnswerFormatter implements OutputFormatter<List<Answer>> {

    @Override
    public String format(List<Answer> answers) {
        return "Enter your answer: ";
    }
}
