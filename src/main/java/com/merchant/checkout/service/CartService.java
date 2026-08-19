package com.merchant.checkout.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.merchant.checkout.model.Cart;
import com.merchant.checkout.model.CheckoutRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.List;

@Service
@Profile("!test")
public class CartService {

    private static final Duration CART_TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public CartService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public Cart save(String cartId, String customerId, List<CheckoutRequest.CartItem> items) {
        Cart cart = new Cart(customerId, items);
        try {
            redisTemplate.opsForValue().set(key(cartId), objectMapper.writeValueAsString(cart), CART_TTL);
            return cart;
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to save cart", exception);
        }
    }

    public Cart find(String cartId) {
        String value = redisTemplate.opsForValue().get(key(cartId));
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, Cart.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to read cart", exception);
        }
    }

    public void delete(String cartId) {
        redisTemplate.delete(key(cartId));
    }

    private String key(String cartId) {
        return "checkout:cart:" + cartId;
    }
}
