package com.merchant.checkout.controller;

import com.merchant.checkout.model.Cart;
import com.merchant.checkout.model.CheckoutRequest;
import com.merchant.checkout.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> saveCart(@Valid @RequestBody CheckoutRequest request) {
        String cartId = UUID.randomUUID().toString();
        cartService.save(cartId, request.getCustomerId(), request.getItems());
        return ResponseEntity.ok(cartId);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable String cartId) {
        Cart cart = cartService.find(cartId);
        return cart == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build();
    }
}
