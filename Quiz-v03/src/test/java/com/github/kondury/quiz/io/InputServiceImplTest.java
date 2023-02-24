package com.github.kondury.quiz.io;

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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InputServiceImplTest {

    @Mock
    private InputStreamProvider inputStreamProvider;
    @InjectMocks
    private InputServiceImpl inputService;

    @Test
    void readWith_shouldReturnValueReturnedByInputStream() {
        String mockedInput = "mocked input";
        InputStream mockedInputStream = new ByteArrayInputStream(mockedInput.getBytes());
        given(inputStreamProvider.getInputStream()).willReturn(mockedInputStream);

        var actualInput = inputService.read();

        assertEquals(mockedInput, actualInput);
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
        assertThrows(InputServiceException.class, () -> inputService.read());
    }
}