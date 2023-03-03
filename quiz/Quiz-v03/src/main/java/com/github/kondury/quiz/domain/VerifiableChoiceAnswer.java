package com.github.kondury.quiz.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerifiableChoiceAnswer extends VerifiableAnswer {

    private final List<Integer> chosenAnswerIndexes;

    public List<Integer> getChosenAnswerIndexes() {
        return Collections.unmodifiableList(chosenAnswerIndexes);
    }

    public VerifiableChoiceAnswer(List<Integer> chosenAnswerIndexes) {
        super(QuestionType.Choice);
        this.chosenAnswerIndexes = new ArrayList<>(chosenAnswerIndexes);
    }
}
