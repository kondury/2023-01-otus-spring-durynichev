package com.github.kondury.quiz.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class InputServiceImpl implements InputService {

    private final InputStreamProvider inputStreamProvider;
    private BufferedReader reader;

    @Override
    public String read() {
        try {
            return getReader().readLine();
        } catch (IOException e) {
            throw new InputServiceException("Error occurred during reading a line.", e);
        }
    }

    private BufferedReader getReader() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(inputStreamProvider.getInputStream()));
        }
        return reader;
    }
}
