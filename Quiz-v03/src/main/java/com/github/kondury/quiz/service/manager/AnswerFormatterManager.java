package com.github.kondury.quiz.service.manager;

import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.io.formatter.AnswerFormatter;
import com.github.kondury.quiz.utils.AbstractManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class AnswerFormatterManager extends AbstractManager<QuestionType, AnswerFormatter> {

    public AnswerFormatterManager(List<? extends AnswerFormatter> formatters) {
        super(formatters.stream()
                .collect(Collectors.toMap(AnswerFormatter::getType, Function.identity()))
        );
    }
}
