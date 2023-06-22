package com.github.kondury.orderprocessing;

import com.github.kondury.orderprocessing.service.MarketplaceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderProcessingFlowApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(OrderProcessingFlowApp.class);
        MarketplaceService marketplaceService = ctx.getBean(MarketplaceService.class);
        marketplaceService.startGenerateOrdersLoop();
    }
}