package com.merchant.checkout.service;

import com.merchant.checkout.model.CheckoutRequest;
import com.merchant.checkout.model.CheckoutResponse;
import com.merchant.checkout.model.Order;
import com.merchant.checkout.repository.OrderRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class CheckoutService {

    private static final Duration IDEMPOTENCY_KEY_TTL = Duration.ofHours(1);

    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;

    public CheckoutService(OrderRepository orderRepository, StringRedisTemplate redisTemplate) {
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
    }

    public CheckoutResponse createOrder(CheckoutRequest request, String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            idempotencyKey = UUID.randomUUID().toString();
        }

        String cacheKey = "checkout:idempotency:" + idempotencyKey;
        String existingOrderId = redisTemplate.opsForValue().get(cacheKey);
        if (existingOrderId != null) {
            return new CheckoutResponse(existingOrderId, "ALREADY_EXISTS");
        }

        String orderId = "ORD-" + UUID.randomUUID();
        Order order = new Order(orderId, request.getCustomerId(), "PENDING");
        orderRepository.save(order);
        redisTemplate.opsForValue().set(cacheKey, orderId, IDEMPOTENCY_KEY_TTL);

        return new CheckoutResponse(orderId, "ACCEPTED");
    }
}
