package com.github.kondury.quiz.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleOutputUtils {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    public String getOut() {
        return outContent.toString().trim();
    }

    public void clear() {
        outContent.reset();
    }
}
