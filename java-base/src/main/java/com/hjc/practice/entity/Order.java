package com.hjc.practice.entity;

import java.math.BigDecimal;

public class Order {
    private String product;
    private BigDecimal amount;

    public Order() {
    }

    public Order(String product, BigDecimal amount) {
        this.product = product;
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
