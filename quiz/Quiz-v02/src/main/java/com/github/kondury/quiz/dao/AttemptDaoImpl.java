package com.github.kondury.quiz.dao;

import com.github.kondury.quiz.domain.AttemptUnit;
import com.github.kondury.quiz.domain.Student;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AttemptDaoImpl implements AttemptDao {

    private final Map<Student, List<AttemptUnit>> attemptUnits = new HashMap<>();

    @Override
    public void register(Student student, AttemptUnit attemptUnit) {
        attemptUnits.computeIfAbsent(student, v -> new ArrayList<>()).add(attemptUnit);
    }

    @Override
    public List<AttemptUnit> getByStudent(Student student) {
        return attemptUnits.get(student);
    }
}
