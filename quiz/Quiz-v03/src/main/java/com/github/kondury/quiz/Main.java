package com.github.kondury.quiz;

import com.github.kondury.quiz.service.QuizService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        QuizService quizService = context.getBean(QuizService.class);
        quizService.run();
    }
}