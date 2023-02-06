package com.github.kondury.quiz.io;

import java.io.PrintStream;

public class ConsoleOutputService implements OutputService {

    private final PrintStream out;

    public ConsoleOutputService(OutputStreamProvider outputStreamProvider) {
        this.out = outputStreamProvider.getPrintStream();
    }

    @Override
    public void output(String text) {
        out.println(text);
    }
}
