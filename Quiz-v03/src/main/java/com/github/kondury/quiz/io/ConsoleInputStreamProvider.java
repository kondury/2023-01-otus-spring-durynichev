package com.github.kondury.quiz.io;

import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ConsoleInputStreamProvider implements InputStreamProvider {

    @Override
    public InputStream getInputStream() {
        return System.in;
    }
}
