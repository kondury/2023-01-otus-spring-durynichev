package com.github.kondury.quiz.io.validator;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableTextAnswer;
import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.kondury.quiz.config.MessageConfig.ILLEGAL_ARGUMENT_VALUE_TMPL_CODE;

@Component
@RequiredArgsConstructor

public class VerifiableTextAnswerValidator implements VerifiableAnswerValidator<VerifiableTextAnswer> {
    private final MessageService messageService;

    @Override
    public ValidationResult validate(String source) {
        if (source == null || source.isBlank()) {
            String message = messageService.getMessage(ILLEGAL_ARGUMENT_VALUE_TMPL_CODE, new String[]{source});
            return new ValidationResult(false, message);
        }
        return new ValidationResult(true, "");
    }

    @Override
    public ValidationResult validate(String source, Question question) {
        return new ValidationResult(true, "");
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Text;
    }
}
