package com.github.kondury.orderprocessing.service.impl;

import com.github.kondury.orderprocessing.model.Article;
import com.github.kondury.orderprocessing.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class MarketplaceCatalogService implements CatalogService {

    private final CopyOnWriteArrayList<Article> articles = new CopyOnWriteArrayList<>(
            Set.of(new Article("coffee"),
                    new Article("tea"),
                    new Article("milk"),
                    new Article("sugar"),
                    new Article("computer"),
                    new Article("playstation"),
                    new Article("phone"))
    );

    @Override
    public int size() {
        return articles.size();
    }

    @Override
    public Article get(int index) {
        return articles.get(index);
    }

    public Collection<Article> findAll() {
        return articles.stream().toList();
    }

    @Override
    public void remove(Article article) {
        log.info("Remove article: {}", article);
        articles.remove(article);
    }
}
