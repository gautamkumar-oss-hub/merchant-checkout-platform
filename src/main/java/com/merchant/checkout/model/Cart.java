package com.merchant.checkout.model;

import java.util.List;

public record Cart(String customerId, List<CheckoutRequest.CartItem> items) {
}
