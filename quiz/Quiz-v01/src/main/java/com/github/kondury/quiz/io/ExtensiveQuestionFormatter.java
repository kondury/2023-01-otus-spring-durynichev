package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExtensiveQuestionFormatter implements QuestionFormatter {

    private int index = 0;
    private final String header;
    private final String footer;
    private final boolean numerate;

    @Override
    public String format(final Question question) {
        StringBuilder builder = new StringBuilder();
        builder.append(header);
        if (numerate) {
            builder.append(++index).append(". ");
        }
        builder.append(question.text()).append('\n');
        builder.append("Answer type: ").append(question.type()).append('\n');

        var answers = question.answers();
        if (QuestionType.Choice == question.type()) {
            builder.append("Answers (correct are marked with '*'):\n");
            int positionIndex = 1;
            for (var answer : answers) {
                String marker = answer.correct() ? "*" : "";
                builder.append("%2s\t%s) %s\n".formatted(marker, positionIndex, answer.text()));
                positionIndex++;
            }
        } else if (QuestionType.Text == question.type()) {
            builder.append("Answer: ");
            builder.append(answers.get(0));
            builder.append('\n');
        }
        builder.append(footer);
        return builder.toString();
    }
}
