package com.github.kondury.quiz.domain;

import com.github.kondury.quiz.utils.Factory;
import org.springframework.stereotype.Component;

@Component
public class StudentFactory implements Factory<Student> {
    @Override
    public Student createFrom(String source) {
        if (source == null || source.isBlank()) {
            throw new EntityCreateException("Illegal argument value: source='%s'".formatted(source));
        }
        String[] nameParts = source.split(" ");
        if (nameParts.length != 2) {
            throw new EntityCreateException("Student name does not consist of " +
                    "two parts separated by single space %s".formatted(source));
        }
        for (var part : nameParts) {
            if (part.length() < 2) {
                throw new EntityCreateException("Each part of student name must" +
                        " have at least 2 symbols: '%s'".formatted(part));
            }
        }
        return new Student(nameParts[0], nameParts[1]);
    }
}
