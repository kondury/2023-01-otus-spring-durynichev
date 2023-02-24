package com.github.kondury.quiz.io;

import java.io.PrintStream;


public class ConsoleOutputStreamProvider implements OutputStreamProvider {

    @Override
    public PrintStream getPrintStream() {
        return System.out;
    }
}
