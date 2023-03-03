package com.github.kondury.quiz.io.formatter;

import com.github.kondury.quiz.domain.Answer;
import com.github.kondury.quiz.domain.QuestionType;
import com.github.kondury.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.kondury.quiz.config.MessageConfig.ATTEMPT_MODE_TEXT_ANSWER_PROMPT_CODE;

@Component
@RequiredArgsConstructor
public class AttemptModeTextAnswerFormatter implements AnswerFormatter {

    private final MessageService messageService;
    @Override
    public String format(List<Answer> answers) {
        return messageService.getMessage(ATTEMPT_MODE_TEXT_ANSWER_PROMPT_CODE);
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Text;
    }
}
