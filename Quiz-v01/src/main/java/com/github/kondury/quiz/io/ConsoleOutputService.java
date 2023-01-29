package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.Question;

import java.io.PrintStream;

public class ConsoleOutputService implements OutputService {

    private final PrintStream out = System.out;

    @Override
    public void output(Question question, QuestionFormatter formatter) {
        out.println(formatter.format(question));
    }
}
