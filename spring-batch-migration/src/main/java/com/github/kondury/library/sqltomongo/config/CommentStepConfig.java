package com.github.kondury.library.sqltomongo.config;

import com.github.kondury.library.mongo.model.CommentDocument;
import com.github.kondury.library.mongo.repository.CommentDocumentRepository;
import com.github.kondury.library.sql.model.Comment;
import com.github.kondury.library.sql.repository.CommentRepository;
import com.github.kondury.library.sqltomongo.service.processor.CommentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.github.kondury.library.sqltomongo.config.BatchConfigUtils.*;

@Configuration
@RequiredArgsConstructor
public class CommentStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final CommentRepository commentRepository;
    private final CommentDocumentRepository commentDocumentRepository;
    private final CommentProcessor commentProcessor;

    @Bean
    public Step commentsStep() {
        return buildStep(
                jobRepository,
                transactionManager,
                "commentsStep",
                commentReader(),
                commentProcessor,
                commentWriter(),
                null);
    }

    @Bean
    public RepositoryItemReader<Comment> commentReader() {
        return buildRepositoryItemReader(commentRepository, "commentReader");
    }

    @Bean
    public RepositoryItemWriter<CommentDocument> commentWriter() {
        return buildRepositoryItemWriter(commentDocumentRepository);
    }
}
