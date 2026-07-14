package com.grisham.mock;

import com.grisham.dto.PriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/mock-api")
public class MockVendorController {

    private static Logger logger = LoggerFactory.getLogger(MockVendorController.class);

    private final Random random = new Random();

    @GetMapping("/amazon/{productId}")
    public PriceDTO getAmazonProductPrice(@PathVariable String productId){
        simulateDelay();
        double price = generatePrice();
        return new PriceDTO(productId,price,"Amazon");
    }

    @GetMapping("flipkart/{productId}")
    public PriceDTO getFlipKartProductPrice(@PathVariable String productId){
        simulateDelay();
        double price = generatePrice();
        return new PriceDTO(productId,price,"Flipkart");
    }

    @GetMapping("walmart/{productId}")
    public PriceDTO getWalmartProductPrice(@PathVariable String productId){
        simulateDelay();
        double price = generatePrice();
        return new PriceDTO(productId,price,"Walmart");
    }

    private void simulateDelay(){
        try {
            Thread.sleep(random.nextInt(100,500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Exception Occurred...");
        }
    }

    private double generatePrice(){
        return Math.round(random.nextDouble(100, 1000) * 100.0) / 100.0;
    }
}
