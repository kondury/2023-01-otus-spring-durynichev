package com.github.kondury.quiz.io;

import com.github.kondury.quiz.utils.Factory;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
public class InputServiceImpl implements InputService {

    private final InputStreamProvider inputStreamProvider;
    private BufferedReader reader;

    @Override
    public <T> T readWith(Factory<T> factory) {
        try {
            String input = getReader().readLine();
            return factory.createFrom(input);
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
