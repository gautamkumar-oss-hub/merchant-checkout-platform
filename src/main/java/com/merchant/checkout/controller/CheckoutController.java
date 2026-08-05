package com.merchant.checkout.controller;

import com.merchant.checkout.model.CheckoutRequest;
import com.merchant.checkout.model.CheckoutResponse;
import com.merchant.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponse> createOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody CheckoutRequest request) {
        CheckoutResponse response = checkoutService.createOrder(request, idempotencyKey);
        return ResponseEntity.accepted().body(response);
    }
}
