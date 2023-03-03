package com.github.kondury.quiz.utils;

import com.github.kondury.quiz.config.CsvDaoSettingsProvider;
import com.github.kondury.quiz.domain.Question;
import com.github.kondury.quiz.domain.QuestionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ResourceFileReaderImplTest {

    @Autowired
    private QuestionFactory questionFactory;

    @Autowired
    private FileReader<Question> questionFileReader;

    @Autowired
    private FileReader<String> stringFileReader;

    @Autowired
    CsvDaoSettingsProvider csvDaoSettingsProvider;

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {"wrongFileName.csv"})
    void load_shouldThrowExceptionWhenFileNameIsWrong(String fileName) {
        assertThrows(ResourceFileException.class, () -> questionFileReader.load(fileName, questionFactory));
    }

    @Test
    void load_shouldReturnListOfObjectsWhenFileHasCorrectFormat() {
        List<String> expected = List.of(
                "*|What is a correct syntax to output \"Hello World\" in Kotlin?|+println(\"Hello World\")|Console.WriteLine(\"Hello World\");|System.out.printline(\"Hello World\")|cout << \"Hello World\";",
                "*|How do you insert COMMENTS in Kotlin code?|+// This is a comment|-- This is a comment|/* This is a comment|# This is comment",
                "T|Which keyword is used to declare a function?|fun",
                "*|In Kotlin, code statements must end with a semicolon (;)|+False|True",
                "*|How can you create a variable with the numeric value 5?|int num = 5|num = 5|+val num = 5|num = 5 int"
        );

        var factory = (Factory<String>) source -> source;
        var result = stringFileReader.load(csvDaoSettingsProvider.getCsvFilename(), factory);
        assertThat(result)
                .hasSize(expected.size())
                .containsAll(expected);
    }

    @Test
    void load_shouldReturnEmptyListWhenQuestionsFileIsEmpty() {
        assertThat(questionFileReader.load("questions-test-empty.csv", questionFactory))
                .hasSize(0);
    }
}
