package com.github.kondury.quiz.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OutputServiceImpl implements OutputService {

    private final OutputStreamProvider outputStreamProvider;

    @Override
    public void output(String text) {
        outputStreamProvider.getPrintStream().println(text);
    }
}
