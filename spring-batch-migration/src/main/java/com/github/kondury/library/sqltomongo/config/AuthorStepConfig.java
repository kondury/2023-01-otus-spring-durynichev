package com.github.kondury.library.sqltomongo.config;

import com.github.kondury.library.mongo.model.AuthorDocument;
import com.github.kondury.library.mongo.repository.AuthorDocumentRepository;
import com.github.kondury.library.sql.model.Author;
import com.github.kondury.library.sql.repository.AuthorRepository;
import com.github.kondury.library.sqltomongo.service.listener.AuthorItemProcessListener;
import com.github.kondury.library.sqltomongo.service.processor.AuthorItemProcessor;
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
public class AuthorStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final AuthorRepository authorRepository;
    private final AuthorDocumentRepository authorDocumentRepository;

    private final AuthorItemProcessor authorItemProcessor;
    private final AuthorItemProcessListener authorListener;

    @Bean
    public Step authorsStep() {
        return buildStep(
                jobRepository,
                transactionManager,
                "authorsStep",
                authorReader(),
                authorItemProcessor,
                authorWriter(),
                authorListener);
    }

    @Bean
    public RepositoryItemReader<Author> authorReader() {
        return buildRepositoryItemReader(authorRepository, "authorReader");
    }

    @Bean
    public RepositoryItemWriter<AuthorDocument> authorWriter() {
        return buildRepositoryItemWriter(authorDocumentRepository);
    }
}
