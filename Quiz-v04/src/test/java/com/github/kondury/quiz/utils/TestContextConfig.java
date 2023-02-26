package com.github.kondury.quiz.utils;

import com.github.kondury.quiz.config.AppProperties;
import com.github.kondury.quiz.domain.QuestionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableConfigurationProperties({AppProperties.class})
@ComponentScan(basePackageClasses = { QuestionFactory.class, ResourceFileReaderImpl.class })
public class TestContextConfig {
}
