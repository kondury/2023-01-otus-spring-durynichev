package com.github.kondury.quiz.utils;

import java.util.List;


public interface FileReader<T> {
    List<T> load(String filename, Factory<T> factory);
}
