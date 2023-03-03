package com.github.kondury.quiz.io;

import java.io.InputStream;

public class ConsoleInputStreamProvider implements InputStreamProvider {

    @Override
    public InputStream getInputStream() {
        return System.in;
    }
}
