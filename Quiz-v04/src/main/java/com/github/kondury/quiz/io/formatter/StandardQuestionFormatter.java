package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.utils.Manager;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class StandardQuestionFormatter implements QuestionFormatter {

    private final Manager<QuestionType, AnswerFormatter> answerFormatterManager;

    @Override
    public String format(final Question question) {
        StringBuilder builder = new StringBuilder();
        builder.append(question.text()).append('\n');
        var answerFormatter = answerFormatterManager.get(question.type());
        builder.append(answerFormatter.format(question.answers()));
        return builder.toString();
    }

}
