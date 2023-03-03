package com.github.kondury.quiz.domain;

import com.github.kondury.quiz.utils.Factory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.github.kondury.quiz.domain.QuestionType.Choice;
import static com.github.kondury.quiz.domain.QuestionType.Text;

@Component
public class QuestionFactory implements Factory<Question> {

    private static final String TEXT_QUESTION_MARKER = "T";
    private static final String CHOICE_QUESTION_MARKER = "*";
    private static final String CORRECT_ANSWER_MARKER = "+";
    private static final String SEPARATOR_REGEX = "\\|";

    @Override
    public Question createFrom(String source) {
        List<String> parts = Arrays.stream(source.split(SEPARATOR_REGEX)).toList();
        if (parts.size() < 3) {
            throw new EntityCreateException(
                    "CSV line must be separated into 3 parts at least. line=%s".formatted(source)
            );
        }
        return create(parts.get(1), parts.get(0), parts.subList(2, parts.size()));
    }

    private Question create(String questionText, String typeMarker, List<String> answerParts) {
        final QuestionType questionType = getQuestionType(typeMarker);
        var answers = answerParts.stream()
                .map(it -> new Answer(trimAnswer(questionType, it), isCorrectAnswer(questionType, it)))
                .toList();
        return new Question(questionText, questionType, answers);
    }

    private QuestionType getQuestionType(String answerType) {
        return switch (answerType) {
            case CHOICE_QUESTION_MARKER -> Choice;
            case TEXT_QUESTION_MARKER -> Text;
            default -> throw new EntityCreateException("Unknown answer type: answerType = %s".formatted(answerType));
        };
    }

    private boolean isCorrectAnswer(QuestionType questionType, String answer) {
        return questionType == Text || answer.startsWith(CORRECT_ANSWER_MARKER);
    }

    private String trimAnswer(QuestionType questionType, String answer) {
        return (questionType == Text || !isCorrectAnswer(questionType, answer))
                ? answer
                : answer.substring(1);
    }

}
