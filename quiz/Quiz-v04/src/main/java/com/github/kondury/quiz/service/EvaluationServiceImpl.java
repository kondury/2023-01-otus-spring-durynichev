package com.github.kondury.quiz.service;

import com.github.kondury.quiz.config.EvaluationSettingsProvider;
import com.github.kondury.quiz.dao.AttemptDao;
import com.github.kondury.quiz.domain.*;
import com.github.kondury.quiz.model.QuizResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final MessageService messageService;
    private final EvaluationSettingsProvider settingsProvider;
    private final AttemptDao attemptDao;

    @Override
    public QuizResult getResults(Student student) {

        final int grade = evaluate(student);
        String messageCode = hasPassed(grade) ? "evaluation.result.passed" : "evaluation.result.didnt-pass";
        String message = messageService.getMessage(messageCode, new Integer[]{grade});
        return new QuizResult(student, true, message);
    }

    private boolean hasPassed(int finalGrade) {
        return finalGrade >= settingsProvider.getMinPercentageToPass();
    }

    private int evaluate(Student student) {
        final List<AttemptUnit> attemptUnits = attemptDao.getByStudent(student);
        if (attemptUnits.size() == 0) {
            return 100;
        }
        final var correctCount = attemptUnits.stream().filter(this::isCorrectAnswer).count();
        return (int) (100.0 * correctCount / attemptUnits.size());
    }

    private boolean isCorrectAnswer(AttemptUnit attemptUnit) {

        return switch (attemptUnit.question().type()) {
            case Text -> isCorrectTextAnswer(attemptUnit);
            case Choice -> isCorrectChoiceAnswer(attemptUnit);
        };
    }

    private static boolean isCorrectChoiceAnswer(AttemptUnit attemptUnit) {
        final var verifiableChoiceAnswer = (VerifiableChoiceAnswer) attemptUnit.verifiableAnswer();
        final var chosenAnswerIndexes = verifiableChoiceAnswer.getChosenAnswerIndexes();
        final var predefinedAnswers = attemptUnit.question().answers();

        long numberOfCorrectAnswers = predefinedAnswers.stream()
                .filter(Answer::correct)
                .count();
        if (numberOfCorrectAnswers != chosenAnswerIndexes.size()) {
            return false;
        }

        return chosenAnswerIndexes.stream()
                .map(chosenIndex -> predefinedAnswers.get(chosenIndex - 1))
                .allMatch(Answer::correct);
    }

    private static boolean isCorrectTextAnswer(AttemptUnit attemptUnit) {
        final var verifiableTextAnswer = (VerifiableTextAnswer) attemptUnit.verifiableAnswer();
        final var studentAnswerText = verifiableTextAnswer.getText();
        final var correctAnswerText = attemptUnit.question().answers().get(0).text();
        return correctAnswerText.equals(studentAnswerText);
    }

}
