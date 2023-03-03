package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.kondury.quiz.config.MessageConfig.ATTEMPT_MODE_CHOICE_ANSWER_PROMPT_CODE;

@Component
@RequiredArgsConstructor
public class AttemptModeChoiceAnswerFormatter implements AnswerFormatter {

    private final MessageService messageService;

    @Override
    public String format(List<Answer> answers) {
        var builder = new StringBuilder();
        int positionIndex = 1;
        for (var answer : answers) {
            builder.append("\t%s) %s\n".formatted(positionIndex, answer.text()));
            positionIndex++;
        }
        builder.append(messageService.getMessage(ATTEMPT_MODE_CHOICE_ANSWER_PROMPT_CODE));
        return builder.toString();
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Choice;
    }
}
