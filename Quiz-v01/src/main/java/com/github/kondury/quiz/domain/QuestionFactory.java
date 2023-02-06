package com.github.kondury.quiz.domain;

import java.util.Arrays;
import java.util.List;

public class QuestionFactory {

    private QuestionFactory() {
    }

    public static Question createFromCsv(String line) {
        List<String> parts = Arrays.stream(line.split("\\|")).toList();
        if (parts.size() < 3) {
            throw new IllegalArgumentException("CSV line has length less then 3. line=%s".formatted(line));
        }
        return create(parts.get(1), parts.get(0), parts.subList(2, parts.size()));
    }

    private static Question create(String questionText, String answerType, List<String> answers) {
        if ("*".equals(answerType)) {
            var answerList = answers.stream()
                    .map(it -> new Answer(trimAnswer(it), isCorrectAnswer(it)))
                    .toList();
            return new Question(questionText, QuestionType.Choice, answerList);
        } else if ("T".equals(answerType)) {
            var answerList = List.of(new Answer(answers.get(0), true));
            return new Question(questionText, QuestionType.Text, answerList);
        } else {
            throw new IllegalArgumentException("Unknown answer type: answerType = %s".formatted(answerType));
        }
    }

    private static Boolean isCorrectAnswer(String answer) {
        return answer.startsWith("+");
    }

    private static String trimAnswer(String answer) {
        if (isCorrectAnswer(answer)) {
            return answer.substring(1);
        } else {
            return answer;
        }
    }

}
