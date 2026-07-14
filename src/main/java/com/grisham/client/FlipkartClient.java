package com.grisham.client;

import com.grisham.dto.PriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class FlipkartClient implements PriceAggregator{

    private static Logger logger = LoggerFactory.getLogger(FlipkartClient.class);

    private final WebClient webClient;
    private final String baseUrl;

    public FlipkartClient(@Qualifier("webClient")WebClient webClient,
                          @Value("${vendors.flipkart.base-url:http://localhost:8080}")String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public double getPrice(String productId) {

        if(logger.isInfoEnabled()){
            logger.info("Fetching {} prices from Flipkart",productId);
        }
        double price = 0;
        try {
            PriceDTO priceDTO = webClient
                    .get()
                    .uri(baseUrl + "/mock-api/flipkart/{productId}",productId)
                    .retrieve()
                    .bodyToMono(PriceDTO.class)
                    .timeout(Duration.ofSeconds(3)).block();
            price = priceDTO.getPrice();
        } catch (Exception e) {
            return handleWebClientException(e);
        }
        return price;
    }

    @Override
    public double getFallbackPrice(String productId) {
        return 0.0;
    }

    private double handleWebClientException(Exception e){
        logger.error("Exception occurred while fetching price from amazon client...{}",e.getMessage());
        return 0.0;
    }
}
