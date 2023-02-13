package com.github.kondury.quiz.service;

import com.github.kondury.quiz.dao.AttemptDao;
import com.github.kondury.quiz.domain.*;
import com.github.kondury.quiz.model.QuizResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final int minPercentageToPass;
    private final AttemptDao attemptDao;

    @Override
    public QuizResult getResults(Student student) {
        final String passedMessageTemplate = "The quiz has been passed successfully. Your final grade is %s%%.";
        final String didntPassMessageTemplate = "The quiz has not been passed. Your final grade is %s%%.";

        final int grade = evaluate(student);

        return hasPassed(grade)
                ? new QuizResult(student, true, passedMessageTemplate.formatted(grade))
                : new QuizResult(student, false, didntPassMessageTemplate.formatted(grade));
    }

    private boolean hasPassed(int finalGrade) {
        return finalGrade >= minPercentageToPass;
    }

    private int evaluate(Student student) {
        final List<AttemptUnit> attemptUnits = attemptDao.getByStudent(student);
        if (attemptUnits.size() == 0) {
            return 100;
        }
        var correctCount = attemptUnits.stream().filter(this::isCorrectAnswer).count();
        return (int) (100.0 * correctCount / attemptUnits.size());
    }

    private boolean isCorrectAnswer(AttemptUnit attemptUnit) {

        return switch (attemptUnit.question().type()) {
            case Text -> {
                var studentAnswer = ((VerifiableTextAnswer) attemptUnit.verifiableAnswer()).text();
                var correctAnswer = attemptUnit.question().answers().get(0).text();
                yield correctAnswer.equals(studentAnswer);
            }
            case Choice -> {
                var studentAnswers = ((VerifiableChoiceAnswer) attemptUnit.verifiableAnswer()).chosenAnswerIndexes();
                var questionAnswers = attemptUnit.question().answers();
                for (int index : studentAnswers) {
                    if (!questionAnswers.get(index - 1).correct()) {
                        yield false;
                    }
                }
                yield questionAnswers.stream().filter(Answer::correct).count() == studentAnswers.size();
            }
        };
    }

}
