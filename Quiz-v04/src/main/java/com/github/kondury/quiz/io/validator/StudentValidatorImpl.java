package com.github.kondury.quiz.io.validator;

import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentValidatorImpl implements StudentValidator {

    private final MessageService messageService;

    @Override
    public ValidationResult validate(String source) {
        if (source == null || source.isBlank()) {
            String message = messageService.getMessage("validation.blank-or-null", new String[]{source});
            return new ValidationResult(false, message);
        }
        String[] nameParts = source.split(" ");
        if (nameParts.length != 2) {
            String message = messageService.getMessage(
                    "acquaintance.input-name.validation.two-parts-required",
                    new String[]{source}
            );
            return new ValidationResult(false, message);
        }
        for (var part : nameParts) {
            if (part.length() < 2) {
                String message = messageService.getMessage(
                        "acquaintance.input-name.validation.two-or-more-symbols-parts",
                        new String[]{source}
                );
                return new ValidationResult(false, message);
            }
        }
        return new ValidationResult(true, "");
    }
}
