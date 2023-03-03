package com.github.kondury.quiz.service;

import com.github.kondury.quiz.config.AppProperties;
import com.github.kondury.quiz.config.QuizServiceSettingsProvider;
import com.github.kondury.quiz.dao.AttemptDao;
import com.github.kondury.quiz.dao.QuizDao;
import com.github.kondury.quiz.domain.*;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.model.QuizResult;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class QuizServiceImplTest {

    @Autowired
    private QuizService quizService;

    @MockBean
    private QuizServiceSettingsProvider quizServiceSettingsProvider;

    @MockBean
    private OutputService outputService;

    @MockBean
    private AcquaintanceService acquaintanceService;

    @MockBean
    private QuizDao quizDao;

    @MockBean
    private AttemptDao attemptDao;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private EvaluationService evaluationService;

    @Configuration
    @EnableConfigurationProperties({AppProperties.class})
    @Import(QuizServiceImpl.class)
    public static class QuizServiceConfiguration {
    }

    @Test
    void run_shouldThrowIllegalStateExceptionWhenReturnedNumberOfQuestionsIsNotEqualToRequired() {
        final int settingsQuestionsNumber = 5;

        final List<Question> mockedEmptyList = new ArrayList<>();
        given(quizDao.getQuestions()).willReturn(mockedEmptyList);
        given(quizServiceSettingsProvider.getQuestionsNumber()).willReturn(settingsQuestionsNumber);
        assertThrows(IllegalStateException.class, () -> quizService.run());
    }

    @Test
    void run_shouldCorrectlyPassArgumentsBetweenServices() {
        final int questionsNumber = 5;

        final List<Question> mockedQuestionList = getMockedQuestions(questionsNumber);
        given(quizDao.getQuestions()).willReturn(mockedQuestionList);
        given(quizServiceSettingsProvider.getQuestionsNumber()).willReturn(questionsNumber);

        final Student mockedStudent = new Student("Firstname", "Lastname");
        given(acquaintanceService.getAcquaintance()).willReturn(mockedStudent);

        final List<AttemptUnit> mockedAttemptUnits = getMockedAttemptUnits(mockedQuestionList);
        for (int i = 0; i < questionsNumber; i++) {
            given(questionService.ask(mockedQuestionList.get(i)))
                    .willReturn(mockedAttemptUnits.get(i));
        }

        final QuizResult mockedQuizResult = new QuizResult(mockedStudent, true, "mocked result message");
        given(evaluationService.getResults(any())).willReturn(mockedQuizResult);

        willDoNothing().given(attemptDao).register(any(), any());
        willDoNothing().given(outputService).output(anyString());

        quizService.run();

        ArgumentCaptor<Question> questionCaptor = ArgumentCaptor.forClass(Question.class);
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        ArgumentCaptor<AttemptUnit> attemptUnitCaptor = ArgumentCaptor.forClass(AttemptUnit.class);

        verify(quizDao, times(1)).getQuestions();
        verify(acquaintanceService, times(1)).getAcquaintance();
        verify(questionService, times(questionsNumber)).ask(questionCaptor.capture());
        verify(attemptDao, times(questionsNumber)).register(studentCaptor.capture(), attemptUnitCaptor.capture());
        verify(evaluationService, times(1)).getResults(studentCaptor.capture());
        verify(outputService, times(1)).output(mockedQuizResult.message());

        assertThat(questionCaptor.getAllValues())
                .hasSize(mockedQuestionList.size())
                .containsExactlyElementsOf(mockedQuestionList);

        var capturedStudents = new HashSet<>(studentCaptor.getAllValues());
        assertThat(capturedStudents)
                .hasSize(1)
                .contains(mockedStudent);

        assertThat(attemptUnitCaptor.getAllValues())
                .hasSize(mockedAttemptUnits.size())
                .containsExactlyElementsOf(mockedAttemptUnits);
    }

    private static List<Question> getMockedQuestions(int size) {
        Question[] questions = new Question[size];
        for (int i = 0; i < 5; i++) {
            questions[i] = new Question(
                    i + " question",
                    QuestionType.Text,
                    List.of(new Answer("answer", true))
            );
        }
        return List.of(questions);
    }

    private static List<AttemptUnit> getMockedAttemptUnits(List<Question> questions) {
        var attemptUnits = new ArrayList<AttemptUnit>();
        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            var answer = new VerifiableTextAnswer(i + " answer");
            attemptUnits.add(new AttemptUnit(question, answer));
        }
        return attemptUnits;
    }
}