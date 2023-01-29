package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.TextAnswer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleOutputServiceTest {

    ConsoleOutputUtils utils = new ConsoleOutputUtils();

    OutputService outputService;

    @BeforeEach
    public void setUp() {
        utils.setUpStreams();
        outputService = new ConsoleOutputService();
    }

    @AfterEach
    public void tearDown() {
        utils.restoreStreams();
    }

    @Test
    void output_SingleQuestionWithHeaderAndFooterFormatted_Resul() {
        Question simpleQuestion = new Question("Question body", new TextAnswer("Answer body"));
        QuestionFormatter formatter = Record::toString;
        outputService.output(simpleQuestion, formatter);
        assertEquals("Question[content=Question body, answer=TextAnswer[answer=Answer body]]", utils.getOut());
    }
}