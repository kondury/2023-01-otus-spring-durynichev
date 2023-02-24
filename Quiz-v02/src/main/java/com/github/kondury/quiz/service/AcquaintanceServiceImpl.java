package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.EntityCreateException;
import com.github.kondury.quiz.domain.Student;
import com.github.kondury.quiz.domain.StudentFactory;
import com.github.kondury.quiz.io.InputService;
import com.github.kondury.quiz.io.OutputService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AcquaintanceServiceImpl implements AcquaintanceService {

    private final OutputService outputService;
    private final InputService inputService;
    private final StudentFactory studentFactory;

    @Override
    public Student getAcquaintance() {
        while (true) {
            try {
                outputService.output("Hello! Please input your firstname and lastname separated by single space:");
                return inputService.readWith(studentFactory);
            } catch (EntityCreateException e) {
                outputService.output(e.getMessage());
            }
        }
    }
}
