package com.github.kondury.orderprocessing.integration;


import com.github.kondury.orderprocessing.model.Order;
import com.github.kondury.orderprocessing.model.ProcessedOrder;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface OrderProcessingGateway {

    @Gateway(requestChannel = "ordersChannel", replyChannel = "processedOrdersChannel")
    ProcessedOrder process(Order order);
}
