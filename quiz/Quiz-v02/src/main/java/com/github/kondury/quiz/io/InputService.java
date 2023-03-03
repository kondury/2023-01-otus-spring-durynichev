package com.github.kondury.quiz.io;

import com.github.kondury.quiz.utils.Factory;

public interface InputService {
    <T> T readWith(Factory<T> factory);
}
