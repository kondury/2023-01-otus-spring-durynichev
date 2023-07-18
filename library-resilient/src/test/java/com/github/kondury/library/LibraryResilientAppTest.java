package com.github.kondury.library;

import com.github.kondury.library.config.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestContainersConfig.class)
class LibraryResilientAppTest {

    @Test
    void contextLoads() {

    }

}