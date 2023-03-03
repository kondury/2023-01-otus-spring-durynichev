package com.github.kondury.quiz.io;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OutputServiceImpl implements OutputService {

    private final OutputStreamProvider outputStreamProvider;

    @Override
    public void output(String text) {
        outputStreamProvider.getPrintStream().println(text);
    }
}
