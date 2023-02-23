package com.github.kondury.quiz.io;

import org.springframework.stereotype.Component;

import java.io.PrintStream;


@Component
public class ConsoleOutputStreamProvider implements OutputStreamProvider {

    @Override
    public PrintStream getPrintStream() {
        return System.out;
    }
}
