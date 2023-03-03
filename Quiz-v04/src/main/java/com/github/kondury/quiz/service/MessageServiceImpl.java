package com.github.kondury.quiz.service;

import com.github.kondury.quiz.config.LocaleProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final LocaleProvider localeProvider;
    private final MessageSource messageSource;
    @Override
    public String getMessage(String messageCode, Object[] args) {
        return messageSource.getMessage(messageCode, args, localeProvider.getLocale());
    }

    @Override
    public String getMessage(String messageCode) {
        return getMessage(messageCode, new Object[]{});
    }
}
