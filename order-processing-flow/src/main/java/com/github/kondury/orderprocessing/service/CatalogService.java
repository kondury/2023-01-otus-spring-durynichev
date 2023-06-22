package com.github.kondury.orderprocessing.service;

import com.github.kondury.orderprocessing.model.Article;

import java.util.Collection;

public interface CatalogService {

    int size();
    Article get(int index);
    Collection<Article> findAll();
    void remove(Article article);
}
