package com.github.kondury.quiz.shell;

import com.github.kondury.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class QuizShell {
    private final QuizService quizService;

    @ShellMethod(value = "Start quiz command")
    public void start() {
        quizService.run();
    }
}
