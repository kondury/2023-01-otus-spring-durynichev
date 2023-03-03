package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class AttemptDaoImplTest {
    private AttemptDaoImpl attemptDao;

    @BeforeEach
    void setUp() {
        attemptDao = new AttemptDaoImpl();
    }

    @Test
    void shouldReturnSingleAddedAttemptUnitWhenSingleAttemptUnitWasAdded() {
        var question = new Question("question", QuestionType.Text, new ArrayList<>());
        var verifiableAnswer = new VerifiableTextAnswer("student answer");
        var attemptUnit = new AttemptUnit(question, verifiableAnswer);
        var student = new Student("Ivan", "Ivanov");
        attemptDao.register(student, attemptUnit);
        var attempts = attemptDao.getByStudent(student);
        assertThat(attempts)
                .hasSize(1)
                .contains(attemptUnit);

    }

    @Test
    void shouldReturnSingleAddedAttemptUnitByStudentWhenSignleAttemptUnitByStudentWasAdded() {
        var question = new Question("question", QuestionType.Text, new ArrayList<>());
        var verifiableAnswer = new VerifiableTextAnswer("student answer");
        var attemptUnit = new AttemptUnit(question, verifiableAnswer);
        var studentIvan = new Student("Ivan", "Ivanov");
        var studentPetr = new Student("Petr", "Petrov");

        attemptDao.register(studentPetr, attemptUnit);
        attemptDao.register(studentIvan, attemptUnit);
        attemptDao.register(studentPetr, attemptUnit);

        var attempts = attemptDao.getByStudent(studentIvan);
        assertThat(attempts)
                .hasSize(1)
                .contains(attemptUnit);

        var attemptsPetr = attemptDao.getByStudent(studentPetr);
        assertThat(attemptsPetr)
                .hasSize(2)
                .contains(attemptUnit);
    }

}