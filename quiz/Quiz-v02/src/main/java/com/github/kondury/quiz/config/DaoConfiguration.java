package com.github.kondury.quiz.config;

import com.github.kondury.quiz.dao.AttemptDao;
import com.github.kondury.quiz.dao.AttemptDaoImpl;
import com.github.kondury.quiz.dao.CsvQuizDao;
import com.github.kondury.quiz.dao.QuizDao;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionFactory;
import com.github.kondury.quiz.utils.FileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfiguration {
    @Bean
    public QuizDao quizDao(
            final @Value("${quiz.csv.filename}:questions.csv") String csvFileName,
            final QuestionFactory questionFactory,
            final FileReader<Question> fileReader
    ) {
        return new CsvQuizDao(csvFileName, questionFactory, fileReader);
    }

    @Bean
    AttemptDao attemptDao() {
        return new AttemptDaoImpl();
    }

}
