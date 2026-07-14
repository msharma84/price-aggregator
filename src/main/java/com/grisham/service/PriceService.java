package com.grisham.service;

import com.grisham.client.PriceAggregator;
import com.grisham.dto.PriceDTO;
import com.grisham.mock.MockVendorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class PriceService {

    private static Logger logger = LoggerFactory.getLogger(PriceService.class);

    private final List<PriceAggregator> priceAggregators;
    private final Executor priceTaskExecutor;
    private final long timeoutMs;

    public PriceService(List<PriceAggregator> priceAggregators,
                        @Qualifier("priceTaskExecutor") Executor priceTaskExecutor,
                        @Value("${price.fetch.timeout-ms:1000}") long timeoutMs) {
        this.priceAggregators = priceAggregators;
        this.priceTaskExecutor = priceTaskExecutor;
        this.timeoutMs = timeoutMs;
    }

    public Map<String,Double> fetchPrice(String productId){

        if(logger.isInfoEnabled()){
            logger.info("Fetching price from services with productId...{}",productId);
        }
        List<CompletableFuture<Double>> futures = priceAggregators.stream()
                .map(aggregator -> getPricesFromClient(aggregator,productId)).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return priceAggregators.stream().collect(Collectors.toMap(
                c -> c.getClass().getSimpleName().replace("Client", "").toLowerCase(),
                c -> futures.get(priceAggregators.indexOf(c)).join()));
    }

    private CompletableFuture<Double> getPricesFromClient(PriceAggregator client, String productId){
        return CompletableFuture.supplyAsync(() -> client.getPrice(productId), priceTaskExecutor)
                .orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> {
                    logger.warn("Failed to fetch price from {}, using fallback",
                            client.getClass().getSimpleName());
                    return client.getFallbackPrice(productId);
                });
    }
}
