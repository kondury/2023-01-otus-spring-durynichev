package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.utils.Manager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StandardQuestionFormatter implements OutputFormatter<Question> {

    private final Manager<QuestionType, OutputFormatter<List<Answer>>> answerFormatterManager;

    @Override
    public String format(final Question question) {
        StringBuilder builder = new StringBuilder();
        builder.append(question.text()).append('\n');
        var answerFormatter = answerFormatterManager.get(question.type());
        builder.append(answerFormatter.format(question.answers()));
        return builder.toString();
    }

}
