package com.merchant.checkout;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.merchant.checkout.service.CartService;
import com.merchant.checkout.service.CheckoutService;

@TestConfiguration
public class TestExternalClients {

    @Bean
    CartService cartService() {
        return new CartService(null, null);
    }

    @Bean
    CheckoutService checkoutService() {
        return new CheckoutService(null, null, null);
    }
}