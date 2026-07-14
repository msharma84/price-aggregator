package com.grisham.dto;

import java.util.Map;

public class PriceDTO {

    private String productId;
    private double price;
    private String vendor;
    private long timestamp;

    public PriceDTO() {
    }

    public PriceDTO(String productId, double price, String vendor) {
        this.productId = productId;
        this.price = price;
        this.vendor = vendor;
        this.timestamp = System.currentTimeMillis();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
