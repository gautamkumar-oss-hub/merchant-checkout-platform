package com.merchant.checkout.messaging;

import com.merchant.checkout.model.Order;
import com.merchant.checkout.repository.OrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
public class OrderEventConsumers {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MeterRegistry meterRegistry;

    public OrderEventConsumers(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;
    }

    @RabbitListener(queues = RabbitConfiguration.ORDER_CREATED_QUEUE)
    @Transactional
    public void validateInventory(OrderCreatedEvent event) {
        orderRepository.findByOrderId(event.orderId()).ifPresent(order -> {
            order.setStatus("INVENTORY_RESERVED");
            orderRepository.save(order);
            meterRegistry.counter("checkout.orders.inventory_reserved").increment();
                rabbitTemplate.convertAndSend(RabbitConfiguration.ORDER_EXCHANGE, RabbitConfiguration.CONFIRMATION_KEY,
                    new OrderCreatedEvent(order.getOrderId()));
        });
    }

    @RabbitListener(queues = RabbitConfiguration.CONFIRMATION_QUEUE)
    @Transactional
    public void confirmOrder(OrderCreatedEvent event) {
        orderRepository.findByOrderId(event.orderId()).ifPresent(order -> {
            order.setStatus("CONFIRMED");
            orderRepository.save(order);
            meterRegistry.counter("checkout.orders.confirmed").increment();
        });
    }
}
