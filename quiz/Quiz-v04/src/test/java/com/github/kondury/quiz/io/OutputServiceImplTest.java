package com.github.kondury.quiz.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class OutputServiceImplTest {

    private OutputService outputService;
    private OutputStream outputStream;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        var outputStreamProvider = mock(OutputStreamProvider.class);
        when(outputStreamProvider.getPrintStream()).thenReturn(printStream);
        outputService = new OutputServiceImpl(outputStreamProvider);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void output_SendTextToOutput_OutputIsTheSame() {
        String text = "Test string";
        outputService.output(text);
        assertEquals(text + System.lineSeparator(), outputStream.toString());
    }
}