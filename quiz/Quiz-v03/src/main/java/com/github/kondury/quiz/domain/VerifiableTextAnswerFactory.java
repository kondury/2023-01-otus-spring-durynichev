package com.github.kondury.quiz.domain;


import org.springframework.stereotype.Component;

@Component
public class VerifiableTextAnswerFactory implements VerifiableAnswerFactory<VerifiableTextAnswer> {

    @Override
    public VerifiableTextAnswer createFrom(String source) {
        if (source == null || source.isBlank()) {
            throw new EntityCreateException("Illegal argument value: source='%s'".formatted(source));
        }
        return new VerifiableTextAnswer(source);
    }

    @Override
    public QuestionType getType() {
        return QuestionType.Text;
    }
}
