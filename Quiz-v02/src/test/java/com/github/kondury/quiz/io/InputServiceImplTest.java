package com.github.kondury.quiz.io;

import com.github.kondury.quiz.domain.EntityCreateException;
import com.github.kondury.quiz.utils.Factory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InputServiceImplTest {

    @SuppressWarnings("rawtypes")
    @Mock
    private Factory factory;
    @Mock
    private InputStreamProvider inputStreamProvider;
    @InjectMocks
    private InputServiceImpl inputService;

    @Test
    void readWith_shouldReturnValueCreatedByFactoryWhenFactoryCreatesObject() {
        final Object readWithExpectedObject = new Object();
        String mockedInput = "mocked input";
        InputStream mockedInputStream = new ByteArrayInputStream(mockedInput.getBytes());
        given(inputStreamProvider.getInputStream()).willReturn(mockedInputStream);
        given(factory.createFrom(anyString())).willReturn(readWithExpectedObject);

        //noinspection unchecked
        var actualResult = inputService.readWith(factory);

        verify(factory).createFrom(eq(mockedInput));
        assertEquals(readWithExpectedObject, actualResult);
    }

    @Test
    void readWith_shouldThrowInputServiceExceptionWhenIOExceptionIsThrown() {
        var throwingIOException = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        given(inputStreamProvider.getInputStream()).willReturn(throwingIOException);
        //noinspection unchecked
        assertThrows(InputServiceException.class, () -> inputService.readWith(factory));
    }

    @Test
    void readWith_shouldThrowEntityCreateExceptionWhenFactoryThrows() {
        String mockedInput = "mocked input";
        InputStream mockedInputStream = new ByteArrayInputStream(mockedInput.getBytes());
        given(inputStreamProvider.getInputStream()).willReturn(mockedInputStream);
        given(factory.createFrom(anyString())).willThrow(EntityCreateException.class);
        //noinspection unchecked
        assertThrows(EntityCreateException.class, () -> inputService.readWith(factory));
    }

}