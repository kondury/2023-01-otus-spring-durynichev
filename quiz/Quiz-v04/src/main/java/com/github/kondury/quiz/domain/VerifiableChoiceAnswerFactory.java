package com.github.kondury.quiz.domain;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class VerifiableChoiceAnswerFactory implements VerifiableAnswerFactory<VerifiableChoiceAnswer> {

    @Override
    public VerifiableChoiceAnswer createFrom(String source) {
        if (source == null || source.isBlank()) {
            throw new EntityCreateException("Illegal argument value: source='%s'".formatted(source));
        }
        try {
            var chosenAnswerIndexes = Arrays.stream(source.split(" ")).map(Integer::parseInt).toList();
            return new VerifiableChoiceAnswer(chosenAnswerIndexes);
        } catch (NumberFormatException e) {
            throw new EntityCreateException("Invalid source string. Source string have to contain only " +
                    "answer indexes separated by a single space: '%s'".formatted(source));
        }
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Choice;
    }
}
