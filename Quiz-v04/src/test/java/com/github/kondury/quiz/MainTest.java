package com.github.kondury.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.config.location=classpath:application-prod-setup.yml"})
@SpringBootTest
class MainTest {
    @Test
    void contextLoads() {
    }

}