package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;

import java.util.List;

public class AttemptModeChoiceAnswerFormatter implements OutputFormatter<List<Answer>> {

    @Override
    public String format(List<Answer> answers) {
        var builder = new StringBuilder();
        int positionIndex = 1;
        for (var answer : answers) {
            builder.append("\t%s) %s\n".formatted(positionIndex, answer.text()));
            positionIndex++;
        }
        builder.append("Choose correct answers and enter their numbers separated by single space: ");
        return builder.toString();
    }
}
