package com.github.kondury.orderprocessing.integration;

import com.github.kondury.orderprocessing.model.Article;
import com.github.kondury.orderprocessing.model.Order;
import com.github.kondury.orderprocessing.model.ProcessedArticle;
import com.github.kondury.orderprocessing.model.ProcessedOrder;
import com.github.kondury.orderprocessing.service.CatalogService;
import com.github.kondury.orderprocessing.service.ProcurementService;
import com.github.kondury.orderprocessing.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Collection;
import java.util.Map;

import static com.github.kondury.orderprocessing.service.ProcurementService.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class IntegrationConfig {

    private static final Map<String, String> statuses = Map.of(
            READY_FOR_SHIPPING, "Ready for shipping",
            LATE_DELIVERY, "Late delivery",
            UNDELIVERABLE, "Undeliverable"
    );

    private final ProcurementService procurementService;
    private final CatalogService catalogService;
    private final WarehouseService warehouseService;

    @Bean
    public QueueChannel ordersChannel() {
        return new QueueChannel(10);
    }

    @Bean
    public PublishSubscribeChannel processedOrdersChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(500).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow orderProcessingFlow() {
        return IntegrationFlow.from("ordersChannel")
                .enrichHeaders(h -> h.headerExpression("orderId", "payload.id"))
                .split(Order.class, Order::articles)
                .<Article, Boolean>route(warehouseService::isAvailable, mapping -> mapping
                        .subFlowMapping(true, flow -> flow.gateway(readyForShippingFlow()))
                        .subFlowMapping(false, flow -> flow.gateway(procurementFlow()))
                )
                .transform(this, "toProcessedArticle")
                .aggregate()
                .transform(this, "toProcessedOrder")
                .channel(processedOrdersChannel())
                .get();
    }

    @Bean
    public IntegrationFlow procurementFlow() {
        return flow -> flow.<Article, String>route(procurementService::getProcurementStatus, mapping -> mapping
                .subFlowMapping(UNDELIVERABLE, sf -> sf.gateway(undeliverableFlow()))
                .subFlowMapping(LATE_DELIVERY, sf -> sf.gateway(lateDeliveryFlow()))
                .subFlowMapping(READY_FOR_SHIPPING, sf -> sf.gateway(readyForShippingFlow())));
    }

    @Bean
    public IntegrationFlow readyForShippingFlow() {
        return flow -> flow.enrichHeaders(
                h -> h.header("status", statuses.get(READY_FOR_SHIPPING)));
    }

    @Bean
    public IntegrationFlow lateDeliveryFlow() {
        return flow -> flow.enrichHeaders(
                h -> h.header("status", statuses.get(LATE_DELIVERY)));
    }

    @Bean
    public IntegrationFlow undeliverableFlow() {
        return flow -> flow
                .publishSubscribeChannel(pubSub -> pubSub
                        .subscribe(sf -> sf
                                .handle(catalogService, "remove")
                                .nullChannel()))
                .enrichHeaders(h -> h.header("status", statuses.get(UNDELIVERABLE)));
    }

    @Transformer
    public ProcessedArticle toProcessedArticle(Article article, @Header("status") String status) {
        log.info("Transforming into Processed Article: {}, {}", article ,status);
        return new ProcessedArticle(article, status);
    }

    @Transformer
    public ProcessedOrder toProcessedOrder(Collection<ProcessedArticle> articles, @Header("orderId") long orderId) {
        log.info("Transforming into Processed Order");
        log.info("\torderId: {}", orderId);
        log.info("\tprocessedArticles: {}", articles);
        return new ProcessedOrder(orderId, articles);
    }
}
