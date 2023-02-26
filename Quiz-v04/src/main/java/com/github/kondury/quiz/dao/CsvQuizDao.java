package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.config.CsvDaoSettingsProvider;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionFactory;
import com.github.kondury.quiz.utils.FileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuizDao implements QuizDao {

    private final CsvDaoSettingsProvider csvDaoSettingsProvider;
    private final QuestionFactory questionFactory;
    private final FileReader<Question> fileReader;

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = fileReader.load(csvDaoSettingsProvider.getCsvFilename(), questionFactory);
        return Collections.unmodifiableList(questions);
    }
}
