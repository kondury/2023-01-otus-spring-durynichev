package com.github.kondury.library.config;

import com.github.kondury.library.migration.LibraryInitializationChangeUnit;
import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "mongock.enabled")
@EnableConfigurationProperties(MongoDbProperties.class)
@Configuration
public class MongockConfig {

    @Bean
    public MongockInitializingBeanRunner getBuilder(
            MongoClient reactiveMongoClient,
            ApplicationContext context,
            MongoDbProperties properties) {
        return MongockSpringboot.builder()
                .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, properties.getDatabase()))
                .addMigrationScanPackage(LibraryInitializationChangeUnit.class.getPackageName())
                .setSpringContext(context)
                .setTransactionEnabled(false)
                .buildInitializingBeanRunner();
    }
}
