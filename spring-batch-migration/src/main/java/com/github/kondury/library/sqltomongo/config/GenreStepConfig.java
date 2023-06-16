package com.github.kondury.library.sqltomongo.config;

import com.github.kondury.library.mongo.model.GenreDocument;
import com.github.kondury.library.mongo.repository.GenreDocumentRepository;
import com.github.kondury.library.sql.model.Genre;
import com.github.kondury.library.sql.repository.GenreRepository;
import com.github.kondury.library.sqltomongo.service.listener.GenreItemProcessListener;
import com.github.kondury.library.sqltomongo.service.processor.GenreProcessor;
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
public class GenreStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final GenreRepository genreRepository;
    private final GenreDocumentRepository genreDocumentRepository;
    private final GenreProcessor genreProcessor;
    private final GenreItemProcessListener genreListener;

    @Bean
    public Step genresStep() {
        return buildStep(
                jobRepository,
                transactionManager,
                "genresStep",
                genreReader(),
                genreProcessor,
                genreWriter(),
                genreListener);
    }

    @Bean
    public RepositoryItemReader<Genre> genreReader() {
        return buildRepositoryItemReader(genreRepository, "genreReader");
    }

    @Bean
    public RepositoryItemWriter<GenreDocument> genreWriter() {
        return buildRepositoryItemWriter(genreDocumentRepository);
    }
}
