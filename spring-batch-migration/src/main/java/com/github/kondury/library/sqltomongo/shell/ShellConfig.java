package com.github.kondury.library.sqltomongo.shell;

import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.EnableCommand;

@EnableCommand(BatchCommands.class)
@Configuration
public class ShellConfig {
}
