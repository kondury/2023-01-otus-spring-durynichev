package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
        builder.append(messageService.getMessage("attempt.answer.choice.prompt"));
        return builder.toString();
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Choice;
    }
}
