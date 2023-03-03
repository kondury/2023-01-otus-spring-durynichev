package com.github.kondury.quiz.config;

import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.io.*;
import com.github.kondury.quiz.utils.FileReader;
import com.github.kondury.quiz.utils.ResourceFileReaderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IOConfiguration {
    @Bean
    public OutputStreamProvider outputStreamProvider() {
        return new ConsoleOutputStreamProvider();
    }

    @Bean
    public OutputServiceImpl outputService(final OutputStreamProvider outputStreamProvider) {
        return new OutputServiceImpl(outputStreamProvider);
    }

    @Bean
    InputStreamProvider inputStreamProvider() {
        return new ConsoleInputStreamProvider();
    }

    @Bean InputService inputService(final InputStreamProvider inputStreamProvider) {
        return new InputServiceImpl(inputStreamProvider);
    }

    @Bean
    public FileReader<Question> resourceFileReader() {
        return new ResourceFileReaderImpl<>();
    }

}
