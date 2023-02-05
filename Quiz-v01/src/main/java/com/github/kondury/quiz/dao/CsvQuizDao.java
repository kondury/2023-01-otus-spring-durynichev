package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionFactory;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuizDao implements QuizDao {

    private final String csvDaoFilename;

    @Override
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(load());
    }

    private List<Question> load() {
        List<Question> questions = new ArrayList<>();
        try (InputStream is = getFileFromResourceAsStream(csvDaoFilename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(QuestionFactory.createFromCsv(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("IO error during file access: %s".formatted(csvDaoFilename), e);
        }
        return questions;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
