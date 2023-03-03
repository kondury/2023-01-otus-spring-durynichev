package com.github.kondury.quiz.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class ResourceFileReaderImpl<T> implements FileReader<T> {

    @Override
    public List<T> load(String filename, Factory<T> factory) {
        try (InputStream is = getFileFromResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {
            return reader.lines().map(factory::createFrom).toList();
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            throw new ResourceFileException("IO error during file access: %s".formatted(filename), e);
        }
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("filename cannot be null or blank! filname='" + fileName + "'");
        }
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
