package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.AttemptUnit;
import com.github.kondury.quiz.domain.Student;

import java.util.List;

public interface AttemptDao {

    void register(Student student, AttemptUnit attemptUnit);
    List<AttemptUnit> getByStudent(Student student);
}
