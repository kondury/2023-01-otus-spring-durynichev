package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.ChoiceAnswer;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.TextAnswer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExtensiveQuestionFormatter implements QuestionFormatter {

    private static int index = 0;
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
        builder.append(question.content());
        builder.append('\n');
        Answer answer = question.answer();
        if (answer instanceof ChoiceAnswer) {
            builder.append("Answer type: choice\n");
            builder.append("Answers (correct are marked with '*'):\n");
            int i = 1;
            for (var e : ((ChoiceAnswer) answer).choices().entrySet()) {
                String marker = e.getValue() ? "*" : "";
                builder.append("%2s\t%s) %s\n".formatted(marker, i, e.getKey()));
                i++;
            }
        } else if (answer instanceof TextAnswer) {
            builder.append("Answer type: text\n");
            builder.append("Answer: ");
            builder.append(((TextAnswer) answer).answer());
            builder.append('\n');
        } else {
            throw new IllegalStateException("Unexpected value: " + answer.getClass());
        }
        builder.append(footer);
        return builder.toString();
    }
}
