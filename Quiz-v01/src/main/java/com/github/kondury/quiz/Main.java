package com.github.kondury.quiz;

import com.github.kondury.quiz.service.QuizService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        QuizService quizService = context.getBean(QuizService.class);
        quizService.run();
    }
}