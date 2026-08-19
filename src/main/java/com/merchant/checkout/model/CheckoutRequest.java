package com.merchant.checkout.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.List;

public class CheckoutRequest {

    @NotEmpty
    private String customerId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<CartItem> items;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public static class CartItem {
        @NotEmpty
        private String productId;

        @NotNull
        @Positive
        private Integer quantity;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
