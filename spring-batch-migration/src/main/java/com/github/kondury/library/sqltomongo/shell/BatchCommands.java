package com.github.kondury.library.sqltomongo.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.command.annotation.Command;

import java.util.Properties;

import static com.github.kondury.library.sqltomongo.config.RdbToMongoJobConfig.LIBRARY_MIGRATION_JOB_NAME;

@Command(group = "Batch Commands")
@RequiredArgsConstructor
class BatchCommands {

    private final Job libraryMigration;

    private final JobLauncher jobLauncher;
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @Command(command = "sjl",
            description = "start library migration from SQL DB to MongoDB using job launcher")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution execution =
                jobLauncher.run(libraryMigration, new JobParametersBuilder()
                        .toJobParameters());
        System.out.println(execution);
    }

    @SuppressWarnings("unused")
    @Command(command = "sjo",
            description = "start library migration from SQL DB to MongoDB using job operator")
    public void startMigrationJobWithJobOperator() throws Exception {
        Properties properties = new Properties();
        Long executionId = jobOperator.start(LIBRARY_MIGRATION_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @Command(command = "info", description = "show last execution info")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(LIBRARY_MIGRATION_JOB_NAME));
    }
}