package com.github.kondury.quiz.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ConsoleOutputServiceTest {

    private final ConsoleOutputUtils utils = new ConsoleOutputUtils();

    private OutputService outputService;

    @BeforeEach
    public void setUp() {
        utils.setUpStreams();
        var outputStreamProvider = new ConsoleOutputStreamProvider();
        outputService = new ConsoleOutputService(outputStreamProvider);
    }

    @AfterEach
    public void tearDown() {
        utils.restoreStreams();
    }

    @Test
    void output_SendTextToOutput_OutputIsTheSame() {
        String text = "Test string";
        outputService.output(text);
        assertEquals(text, utils.getOut());
    }
}