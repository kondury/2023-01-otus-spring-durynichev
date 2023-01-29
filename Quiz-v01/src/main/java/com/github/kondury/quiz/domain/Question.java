package com.github.kondury.quiz.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record Question(String content, Answer answer) {

    public static class Factory {

        private Factory() {}

        public static Question createFromCsv(String line) {
            List<String> parts = Arrays.stream(line.split("\\|")).toList();
            if (parts.size() < 3) {
                throw new IllegalArgumentException("CSV line has length less then 3. line=%s".formatted(line));
            }
            return create(parts.get(1), parts.get(0), parts.subList(2, parts.size()));
        }

        private static Question create(String content, String answerType, List<String> answers) {
            if ("*".equals(answerType)) {
                var map = answers.stream().collect(Collectors.toMap(Factory::trimAnswer, Factory::isCorrectAnswer));
                return new Question(content, new ChoiceAnswer(map));
            } else if ("T".equals(answerType)) {
                return new Question(content, new TextAnswer(answers.get(0)));
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
}
