package com.github.kondury.quiz.service;

import com.github.kondury.quiz.config.QuizProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final QuizProperties quizProperties;
    private final MessageSource messageSource;
    @Override
    public String getMessage(String messageCode, Object[] args) {
        return messageSource.getMessage(messageCode, args, quizProperties.getLocale());
    }

    @Override
    public String getMessage(String messageCode) {
        return getMessage(messageCode, new Object[]{});
    }
}
