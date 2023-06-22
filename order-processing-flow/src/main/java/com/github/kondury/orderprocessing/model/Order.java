package com.github.kondury.orderprocessing.model;

import java.util.Collection;


public record Order(long id, Collection<Article> articles) {
}
