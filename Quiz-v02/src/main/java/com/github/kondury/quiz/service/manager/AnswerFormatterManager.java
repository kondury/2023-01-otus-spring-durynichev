package com.github.kondury.quiz.service.manager;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.io.formatter.OutputFormatter;
import com.github.kondury.quiz.utils.AbstractManager;

import java.util.List;
import java.util.Map;

public class AnswerFormatterManager extends AbstractManager<QuestionType, OutputFormatter<List<Answer>>> {

    public AnswerFormatterManager(Map<QuestionType, OutputFormatter<List<Answer>>> strategies) {
        super(strategies);
    }
}
