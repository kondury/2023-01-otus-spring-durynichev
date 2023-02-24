package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Question;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnhancedQuestionFormatter implements QuestionFormatter {

    private final QuestionFormatter formatter;
    private final String header;
    private final String footer;
    private final boolean isSequential;

    private int index = 0;

    @Override
    public String format(Question question) {
        StringBuilder builder = new StringBuilder();
        builder.append(header);
        if (isSequential) {
            builder.append(++index).append(". ");
        }
        builder.append(formatter.format(question));
        builder.append(footer);
        return builder.toString();
    }
}
