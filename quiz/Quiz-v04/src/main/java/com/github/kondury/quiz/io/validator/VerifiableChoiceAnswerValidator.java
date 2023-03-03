package com.github.kondury.quiz.io.validator;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.domain.VerifiableChoiceAnswer;
import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class VerifiableChoiceAnswerValidator implements VerifiableAnswerValidator<VerifiableChoiceAnswer> {

    private final MessageService messageService;

    @Override
    public ValidationResult validate(String source) {
        if (source == null || source.isBlank()) {
            String message = messageService.getMessage("validation.blank-or-null", new String[]{source});
            return new ValidationResult(false, message);
        }
        return new ValidationResult(true, "");
    }

    @Override
    public ValidationResult validate(String source, Question question) {
        var validationResult = validate(source);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        try {
            final int minAnswerIndex = 1;
            final int maxAnswerIndex = question.answers().size();
            final List<Integer> wrongIndexes = Arrays.stream(source.split(" "))
                    .map(Integer::parseInt)
                    .filter(it -> it < minAnswerIndex || it > maxAnswerIndex)
                    .toList();
            if (wrongIndexes.size() != 0) {
                String message = messageService.getMessage(
                        "attempt.answer.choice.validation.index-out-of-range",
                        new Object[]{
                                wrongIndexes.stream().map(String::valueOf).collect(Collectors.joining(", ")),
                                maxAnswerIndex
                        }
                );
                return new ValidationResult(false, message);
            }
        } catch (NumberFormatException e) {
            String message = messageService.getMessage(
                    "attempt.answer.choice.validation.invalid-input",
                    new String[] { source }
            );
            return new ValidationResult(false, message);
        }
        return new ValidationResult(true, "");
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Choice;
    }
}
