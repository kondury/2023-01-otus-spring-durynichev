package com.github.kondury.orderprocessing.service.impl;

import com.github.kondury.orderprocessing.model.Article;
import com.github.kondury.orderprocessing.service.ProcurementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import static com.github.kondury.orderprocessing.service.impl.Utils.delay;

@Service
@Slf4j
public class ProcurementServiceImpl implements ProcurementService {

    private static final long MILLIS_TO_CHECK = 200;
    private static final int READY_FOR_SHIPPING_CHANCE_UPPER_BORDER = 10;
    private static final int LATE_DELIVERY_CHANCE_UPPER_BORDER = READY_FOR_SHIPPING_CHANCE_UPPER_BORDER + 50;

    @Override
    public String getProcurementStatus(Article article) {
        delay(MILLIS_TO_CHECK);
        log.info("Checking procurement status: {}", article);

        int chance = RandomUtils.nextInt(0, 100);
        String status = UNDELIVERABLE;
        if (chance <= READY_FOR_SHIPPING_CHANCE_UPPER_BORDER) {
            status = READY_FOR_SHIPPING;
        } else if (chance <= LATE_DELIVERY_CHANCE_UPPER_BORDER) {
            status = LATE_DELIVERY;
        }

        log.info("\tstatus: {}", status);

        return status;
    }
}
