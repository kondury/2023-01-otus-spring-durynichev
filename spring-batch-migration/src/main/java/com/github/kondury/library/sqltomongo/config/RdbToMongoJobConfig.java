package com.github.kondury.library.sqltomongo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@RequiredArgsConstructor
public class RdbToMongoJobConfig {
    public static final String LIBRARY_MIGRATION_JOB_NAME = "sqlToMongoLibraryMigration";
    public static final int CHUNK_SIZE = 5;

    private final JobRepository jobRepository;

    @Bean
    public Job libraryMigration(Flow authorsAndGenresFlow, Step booksStep, Step commentsStep) {
        return new JobBuilder(LIBRARY_MIGRATION_JOB_NAME, jobRepository)
                .start(authorsAndGenresFlow)
                .next(booksStep)
                .next(commentsStep)
                .end()
                .build();
    }

    @Bean
    public Flow authorsAndGenresFlow(Flow authorsFlow, Flow genresFlow) {
        return new FlowBuilder<SimpleFlow>("authorsAndGenresFlow")
                .split(taskExecutor())
                .add(authorsFlow, genresFlow)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }


    @Bean
    public Flow authorsFlow(Step authorsStep) {
        return buildWrapperFlow("authorsFlow", authorsStep);
    }

    @Bean
    public Flow genresFlow(Step genresStep) {
        return buildWrapperFlow("genresFlow", genresStep);
    }

    private static Flow buildWrapperFlow(String flowName, Step step) {
        return new FlowBuilder<SimpleFlow>(flowName)
                .start(step)
                .build();
    }
}
