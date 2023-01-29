package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.Question;

public interface OutputService {
    void output(final Question question, final QuestionFormatter formatter);

    default void output(final Question question, final QuestionFormatter formatter, final String prefix, final String suffix) {
        output(question, it -> prefix + formatter.format(it) + suffix);
    }

    default void output(final Question question) {
        output(question, Record::toString);
    }
}
