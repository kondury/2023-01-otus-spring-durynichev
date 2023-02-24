package com.github.kondury.quiz.io.validator;

import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.kondury.quiz.config.MessageConfig.*;

@Component
@RequiredArgsConstructor
public class StudentValidatorImpl implements StudentValidator {

    private final MessageService messageService;

    @Override
    public ValidationResult validate(String source) {
        if (source == null || source.isBlank()) {
            String message = messageService.getMessage(ILLEGAL_ARGUMENT_VALUE_TMPL_CODE, new String[]{source});
            return new ValidationResult(false, message);
        }
        String[] nameParts = source.split(" ");
        if (nameParts.length != 2) {
            String message = messageService.getMessage(TWO_PARTS_IN_NAME_TMPL_CODE, new String[]{source});
            return new ValidationResult(false, message);
        }
        for (var part : nameParts) {
            if (part.length() < 2) {
                String message = messageService.getMessage(
                        EACH_PART_MUST_BE_MORE_THAN_TWO_SYMBOLS_CODE,
                        new String[]{source}
                );
                return new ValidationResult(false, message);
            }
        }
        return new ValidationResult(true, "");
    }
}
