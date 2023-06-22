package com.github.kondury.orderprocessing.service.impl;

import com.github.kondury.orderprocessing.model.Article;
import com.github.kondury.orderprocessing.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import static com.github.kondury.orderprocessing.service.impl.Utils.delay;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private static final double AVAILABILITY_CHANCE = 0.7;
    private static final long MILLIS_TO_CHECK = 200;

    @Override
    public boolean isAvailable(Article article) {
        delay(MILLIS_TO_CHECK);

        log.info("Checking availability: {}", article);
        boolean isAvailable = RandomUtils.nextDouble(0.0, 1.0) <= AVAILABILITY_CHANCE;
        log.info("\tavailable: {}", isAvailable);
        return isAvailable;
    }
}
