package com.github.kondury.orderprocessing.service.impl;

import com.github.kondury.orderprocessing.model.Article;
import com.github.kondury.orderprocessing.model.Order;
import com.github.kondury.orderprocessing.model.ProcessedOrder;
import com.github.kondury.orderprocessing.integration.OrderProcessingGateway;
import com.github.kondury.orderprocessing.service.CatalogService;
import com.github.kondury.orderprocessing.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.IntStream;

import static com.github.kondury.orderprocessing.service.impl.Utils.delay;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketplaceServiceImpl implements MarketplaceService {

    private static final long MILLIS_BETWEEN_ORDERS = 100;
    private static final int  ORDERS_TO_GENERATE = 3;
    private static final int MAX_ARTICLES_PER_ORDER = 5;
    private static long orderCount = 0;

    private final CatalogService catalogService;
    private final OrderProcessingGateway orderProcessingGateway;

    public void startGenerateOrdersLoop() {
        for (int i = 0; i < ORDERS_TO_GENERATE; i++) {
            var order = generateOrder();
            log.info("New order: {}", order);
            ProcessedOrder response = orderProcessingGateway.process(order);
            log.info("Processed order: {}", response);
            delay(MILLIS_BETWEEN_ORDERS);
        }
    }

    private Order generateOrder() {
        var articlesInOrder = RandomUtils.nextInt(1, MAX_ARTICLES_PER_ORDER + 1);
        return new Order(++orderCount, generateArticles(articlesInOrder));
    }

    private Collection<Article> generateArticles(int quantityToGenerate) {
        return IntStream.rangeClosed(1, quantityToGenerate)
                .mapToObj(it -> getRandomArticle(catalogService))
                .toList();
    }

    private static Article getRandomArticle(CatalogService service) {
        return service.get(RandomUtils.nextInt(0, service.size()));
    }


}