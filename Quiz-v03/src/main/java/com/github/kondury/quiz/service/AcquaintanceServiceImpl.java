package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.Student;
import com.github.kondury.quiz.domain.StudentFactory;
import com.github.kondury.quiz.io.InputService;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.io.validator.StudentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.kondury.quiz.config.MessageConfig.INPUT_STUDENT_NAME_PROMPT_CODE;
import static com.github.kondury.quiz.config.MessageConfig.WELCOME_MESSAGE_CODE;

@Component
@RequiredArgsConstructor
public class AcquaintanceServiceImpl implements AcquaintanceService {

    private final MessageService messageService;
    private final OutputService outputService;
    private final InputService inputService;
    private final StudentFactory studentFactory;
    private final StudentValidator validator;

    @Override
    public Student getAcquaintance() {
        outputService.output(messageService.getMessage(WELCOME_MESSAGE_CODE));
        while (true) {
            outputService.output(messageService.getMessage(INPUT_STUDENT_NAME_PROMPT_CODE));
            var input = inputService.read();
            var validationResult = validator.validate(input);
            if (!validationResult.isValid()) {
                outputService.output(validationResult.message());
                continue;
            }
            return studentFactory.createFrom(input);
        }
    }
}
