package com.grisham.client;

public interface PriceAggregator {

    double getPrice(String productId);

    double getFallbackPrice(String productId);
}
