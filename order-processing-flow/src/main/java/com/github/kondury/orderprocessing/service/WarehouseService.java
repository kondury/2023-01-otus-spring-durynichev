package com.github.kondury.orderprocessing.service;

import com.github.kondury.orderprocessing.model.Article;

public interface WarehouseService {

    boolean isAvailable(Article article);
}
