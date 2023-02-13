package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionFactory;
import com.github.kondury.quiz.utils.FileReader;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuizDao implements QuizDao {

    private final String csvFilename;
    private final QuestionFactory questionFactory;
    private final FileReader<Question> fileReader;

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = fileReader.load(csvFilename, questionFactory);
        return Collections.unmodifiableList(questions);
    }
}
