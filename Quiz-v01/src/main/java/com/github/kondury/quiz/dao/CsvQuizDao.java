package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvQuizDao implements QuizDao {

    private static final String CSV_DAO_FILENAME = "questions.csv";

    private final List<Question> questions;

    public CsvQuizDao() throws IOException {
        questions = CsvQuestionsLoader.load(CSV_DAO_FILENAME);
    }

    @Override
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    private static class CsvQuestionsLoader {

        public static List<Question> load(String csvDaoFilename) throws IOException {
            List<Question> questions = new ArrayList<>();
            try (InputStream is = getFileFromResourceAsStream(csvDaoFilename);
                 InputStreamReader streamReader = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(streamReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    questions.add(Question.Factory.createFromCsv(line));
                }
            }
            return questions;
        }

        private static InputStream getFileFromResourceAsStream(String fileName) {
            ClassLoader classLoader = CsvQuestionsLoader.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName);

            if (inputStream == null) {
                throw new IllegalArgumentException("file not found! " + fileName);
            } else {
                return inputStream;
            }
        }
    }
}
