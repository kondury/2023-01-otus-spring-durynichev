package com.github.kondury.library.sqltomongo.config;

import com.github.kondury.library.mongo.model.BookDocument;
import com.github.kondury.library.mongo.repository.BookDocumentRepository;
import com.github.kondury.library.sql.model.Book;
import com.github.kondury.library.sql.repository.BookRepository;
import com.github.kondury.library.sqltomongo.service.listener.BookItemProcessListener;
import com.github.kondury.library.sqltomongo.service.processor.BookProcessor;
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
public class BookStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;

    @Bean
    public Step booksStep(BookProcessor bookProcessor,
                            BookItemProcessListener bookListener) {
        return buildStep(
                jobRepository,
                transactionManager,
                "booksStep",
                bookReader(),
                bookProcessor,
                bookWriter(),
                bookListener);
    }

    @Bean
    public RepositoryItemReader<Book> bookReader() {
        return buildRepositoryItemReader(bookRepository, "bookReader");
    }

    @Bean
    public RepositoryItemWriter<BookDocument> bookWriter() {
        return buildRepositoryItemWriter(bookDocumentRepository);
    }
}
