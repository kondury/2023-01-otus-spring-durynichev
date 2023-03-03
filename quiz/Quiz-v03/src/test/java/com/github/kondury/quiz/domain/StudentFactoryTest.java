package com.github.kondury.quiz.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StudentFactoryTest {

    private final StudentFactory studentFactory = new StudentFactory();

    @ParameterizedTest
    @CsvSource({
            "FirstName LastName, FirstName, LastName",
            "Fi La, Fi, La"
    })
    void createFrom_shouldReturnStudentFromCorrectSource(
            String source,
            String expectedFirstName,
            String expectedLastName) {
        var student = studentFactory.createFrom(source);
        var firstName = student.firstName();
        var lastName = student.lastName();
        assertEquals(expectedFirstName, firstName);
        assertEquals(expectedLastName, lastName);
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void createFrom_shouldThrowEntityCreateExceptionWhenIllegalArgumentPassed(String source) {
        String messageBeginning = "Illegal argument value: source";
        Throwable result = assertThrows(EntityCreateException.class, () -> studentFactory.createFrom(source));
        assertThat(result.getMessage()).startsWith(messageBeginning);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "WithoutSecondName",
            "More Then Two",
            "TooManySpaces  InTheMiddle",
            " TooManySpacesInBeginning Two",
    })
    void createFrom_shouldThrowEntityCreateExceptionWhenWrongFormatOrTooManyParts(String source) {
        String messageBeginning = "Student name does not consist of two parts separated by single space";
        Throwable result = assertThrows(EntityCreateException.class, () -> studentFactory.createFrom(source));
        assertThat(result.getMessage()).startsWith(messageBeginning);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "F L",
            "LastNameIsTooShort L",
            "F FirstNameIsTooShort"
    })
    void createFrom_shouldThrowEntityCreateExceptionWhenPartsAreTooShort(String source) {
        String messageBeginning = "Each part of student name must have at least 2 symbols";
        Throwable result = assertThrows(EntityCreateException.class, () -> studentFactory.createFrom(source));
        assertThat(result.getMessage()).startsWith(messageBeginning);
    }

}