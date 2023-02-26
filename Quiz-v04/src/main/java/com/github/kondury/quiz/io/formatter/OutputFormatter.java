package com.github.kondury.quiz.io.formatter;

@FunctionalInterface
public interface OutputFormatter<T> {

    String format(final T t);
}
