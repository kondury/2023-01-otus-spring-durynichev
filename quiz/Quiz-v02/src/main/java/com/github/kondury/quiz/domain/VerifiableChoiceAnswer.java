package com.github.kondury.quiz.domain;

import java.util.List;

public record VerifiableChoiceAnswer(List<Integer> chosenAnswerIndexes) implements VerifiableAnswer {
}
