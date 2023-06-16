package com.github.kondury.library.sqltomongo.config;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

import static com.github.kondury.library.sqltomongo.config.RdbToMongoJobConfig.CHUNK_SIZE;

public class BatchConfigUtils {

    public static  <I, O> Step buildStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            String stepName,
            ItemReader<I> reader,
            ItemProcessor<I, O> processor,
            ItemWriter<O> writer,
            @Nullable ItemProcessListener<I, O> listener
    ) {
        var stepBuilder = new StepBuilder(stepName, jobRepository)
                .<I, O>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer);
        if (listener != null) {
            stepBuilder.listener(listener);
        }
        return stepBuilder.build();
    }

    public static <I, ID> RepositoryItemReader<I> buildRepositoryItemReader(
            PagingAndSortingRepository<I, ID> repository,
            String name) {
        return new RepositoryItemReaderBuilder<I>()
                .name(name)
                .repository(repository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    public static  <O, ID> RepositoryItemWriter<O> buildRepositoryItemWriter(MongoRepository<O, ID> repository) {
        return new RepositoryItemWriterBuilder<O>()
                .repository(repository)
                .build();
    }
}
