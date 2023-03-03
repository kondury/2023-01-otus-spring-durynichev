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

import static com.github.kondury.quiz.config.MessageConfig.ILLEGAL_ARGUMENT_VALUE_TMPL_CODE;
import static com.github.kondury.quiz.config.MessageConfig.INVALID_SOURCE_STRING_TMPL_CODE;
import static com.github.kondury.quiz.config.MessageConfig.INDEX_OUT_OF_RANGE_TMPL_CODE;


@Component
@RequiredArgsConstructor
public class VerifiableChoiceAnswerValidator implements VerifiableAnswerValidator<VerifiableChoiceAnswer> {

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
                        INDEX_OUT_OF_RANGE_TMPL_CODE,
                        new Object[]{
                                wrongIndexes.stream().map(String::valueOf).collect(Collectors.joining(", ")),
                                maxAnswerIndex
                        }
                );
                return new ValidationResult(false, message);
            }
        } catch (NumberFormatException e) {
            String message = messageService.getMessage(INVALID_SOURCE_STRING_TMPL_CODE, new String[]{source});
            return new ValidationResult(false, message);
        }
        return new ValidationResult(true, "");
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Choice;
    }
}
