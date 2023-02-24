package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.config.QuizProperties;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionFactory;
import com.github.kondury.quiz.utils.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CsvQuizDaoTest {

    private QuizProperties quizProperties;
    private QuestionFactory questionFactory;
    private FileReader<Question> fileReader;

    private CsvQuizDao csvQuizDao;

    @BeforeEach
    void setUp() {
        questionFactory = mock(QuestionFactory.class);
        //noinspection unchecked
        fileReader = (FileReader<Question>) mock(FileReader.class);
        quizProperties = mock(QuizProperties.class);

        csvQuizDao = new CsvQuizDao(quizProperties, questionFactory, fileReader);
    }

    @Test
    void getQuestions_shouldReturnResultListFromFileReaderBeingCalledWithExpectedArguments() {
        List<Question> questions = new ArrayList<>();
        given(fileReader.load(anyString(), any())).willReturn(questions);
        String csvFilename = "mocked filename";
        given(quizProperties.getCsvFilename()).willReturn(csvFilename);

        var result = csvQuizDao.getQuestions();

        verify(fileReader).load(eq(csvFilename), eq(questionFactory));
        assertThat(result).isEqualTo(questions);
    }
}