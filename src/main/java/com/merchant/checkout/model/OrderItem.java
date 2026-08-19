package com.merchant.checkout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private Integer quantity;

    protected OrderItem() {
    }

    public OrderItem(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
