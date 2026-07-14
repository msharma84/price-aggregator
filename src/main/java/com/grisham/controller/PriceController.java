package com.grisham.controller;

import com.grisham.dto.PriceDTO;
import com.grisham.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/price")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String,Double>> getPrices(@PathVariable String productId){
        Map<String, Double> pricesMap = priceService.fetchPrice(productId);
        return ResponseEntity.ok(pricesMap);
    }
}
