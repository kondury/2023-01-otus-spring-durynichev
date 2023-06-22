package com.github.kondury.orderprocessing.model;

import java.util.Collection;

public record ProcessedOrder(long orderId, Collection<ProcessedArticle> articles) {
}
