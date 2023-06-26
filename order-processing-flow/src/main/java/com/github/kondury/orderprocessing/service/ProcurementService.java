package com.github.kondury.orderprocessing.service;

import com.github.kondury.orderprocessing.model.Article;

public interface ProcurementService {

    String READY_FOR_SHIPPING = "READY_FOR_SHIPPING";
    String LATE_DELIVERY = "LATE_DELIVERY";
    String UNDELIVERABLE = "UNDELIVERABLE";

    String getProcurementStatus(Article article);
}
