package com.merchant.checkout.service;

import com.merchant.checkout.model.CheckoutRequest;
import com.merchant.checkout.model.CheckoutResponse;
import com.merchant.checkout.model.Order;
import com.merchant.checkout.model.OrderItem;
import com.merchant.checkout.messaging.OrderCreatedEvent;
import com.merchant.checkout.messaging.RabbitConfiguration;
import com.merchant.checkout.repository.OrderRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.UUID;

@Service
@Profile("!test")
public class CheckoutService {

    private static final Duration IDEMPOTENCY_KEY_TTL = Duration.ofHours(1);

    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public CheckoutService(OrderRepository orderRepository, StringRedisTemplate redisTemplate, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
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
        Boolean reserved = redisTemplate.opsForValue().setIfAbsent(cacheKey, orderId, IDEMPOTENCY_KEY_TTL);
        if (!Boolean.TRUE.equals(reserved)) {
            return new CheckoutResponse(redisTemplate.opsForValue().get(cacheKey), "ALREADY_EXISTS");
        }
        Order order = new Order(orderId, request.getCustomerId(), "PENDING");
        order.setItems(request.getItems().stream()
            .map(item -> new OrderItem(item.getProductId(), item.getQuantity()))
            .toList());
        orderRepository.save(order);
        rabbitTemplate.convertAndSend(RabbitConfiguration.ORDER_EXCHANGE, RabbitConfiguration.ORDER_CREATED_KEY,
            new OrderCreatedEvent(orderId));

        return new CheckoutResponse(orderId, "ACCEPTED");
    }
}
