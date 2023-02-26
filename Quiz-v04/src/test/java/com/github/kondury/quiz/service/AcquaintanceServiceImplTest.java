package com.github.kondury.quiz.service;

import com.github.kondury.quiz.domain.EntityCreateException;
import com.github.kondury.quiz.domain.StudentFactory;
import com.github.kondury.quiz.io.InputService;
import com.github.kondury.quiz.io.OutputService;
import com.github.kondury.quiz.io.validator.StudentValidator;
import com.github.kondury.quiz.io.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class AcquaintanceServiceImplTest {

    @Mock
    private MessageService messageService;
    @Mock
    private OutputService outputService;
    @Mock
    private InputService inputService;
    @Mock
    private StudentFactory studentFactory;
    @Mock
    private StudentValidator validator;

    @InjectMocks
    private AcquaintanceServiceImpl acquaintanceService;

    @Test
    void getAcquaintance_shouldThrowEntityCreateExceptionWhenFactoryThrows() {
        String mockedInput = "mocked input";
        String mockedMessage = "mocked message";
        ValidationResult okValidationResult = new ValidationResult(true, "");
        given(messageService.getMessage(anyString())).willReturn(mockedMessage);
        doNothing().when(outputService).output(anyString());
        given(inputService.read()).willReturn(mockedInput);
        given(validator.validate(anyString())).willReturn(okValidationResult);
        given(studentFactory.createFrom(anyString())).willThrow(EntityCreateException.class);

        assertThrows(EntityCreateException.class, () -> acquaintanceService.getAcquaintance());
    }
}