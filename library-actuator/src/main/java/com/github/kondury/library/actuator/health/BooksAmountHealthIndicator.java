package com.github.kondury.library.actuator.health;

import com.github.kondury.library.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BooksAmountHealthIndicator implements HealthIndicator {

    private static final long MIN_BOOKS_AMOUNT = 3L;

    private final BookService bookService;

    @Override
    public Health health() {
        var booksAmount = bookService.count();
        if (booksAmount < MIN_BOOKS_AMOUNT) {
            log.warn("Books amount in the library fell below the threshold {} and is now {}",
                    MIN_BOOKS_AMOUNT, booksAmount);
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Books amount in the library fell below the threshold")
                    .withDetail("booksAmount", booksAmount)
                    .build();
        } else {
            return Health.up()
                    .withDetail("booksAmount", booksAmount)
                    .build();
        }
    }
}
