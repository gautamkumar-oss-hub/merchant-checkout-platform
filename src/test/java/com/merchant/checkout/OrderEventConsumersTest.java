package com.merchant.checkout;

import com.merchant.checkout.messaging.OrderCreatedEvent;
import com.merchant.checkout.messaging.OrderEventConsumers;
import com.merchant.checkout.messaging.RabbitConfiguration;
import com.merchant.checkout.model.Order;
import com.merchant.checkout.repository.OrderRepository;
import com.merchant.checkout.model.OrderItem;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderEventConsumersTest {

    @Test
    void reservesInventoryAndPublishesConfirmation() {
        Order order = order();
        RecordingRepository repository = new RecordingRepository(order);
        RecordingRabbitTemplate rabbitTemplate = new RecordingRabbitTemplate();
        SimpleMeterRegistry meters = new SimpleMeterRegistry();
        OrderEventConsumers consumers = new OrderEventConsumers(repository.repository(), rabbitTemplate, meters);

        consumers.validateInventory(new OrderCreatedEvent(order.getOrderId()));

        assertEquals("INVENTORY_RESERVED", order.getStatus());
        assertEquals(RabbitConfiguration.CONFIRMATION_KEY, rabbitTemplate.routingKey);
        assertEquals(1.0, meters.get("checkout.orders.inventory_reserved").counter().count());
    }

    @Test
    void confirmsOrderAndRecordsMetric() {
        Order order = order();
        RecordingRepository repository = new RecordingRepository(order);
        SimpleMeterRegistry meters = new SimpleMeterRegistry();
        OrderEventConsumers consumers = new OrderEventConsumers(repository.repository(), new RecordingRabbitTemplate(), meters);

        consumers.confirmOrder(new OrderCreatedEvent(order.getOrderId()));

        assertEquals("CONFIRMED", order.getStatus());
        assertEquals(1.0, meters.get("checkout.orders.confirmed").counter().count());
        assertTrue(repository.saved);
    }

    private Order order() {
        Order order = new Order("ORD-TEST-1", "customer-1", "PENDING");
        order.setItems(List.of(new OrderItem("product-1", 1)));
        return order;
    }

    private static class RecordingRepository {
        private final Order order;
        private boolean saved;
        private final OrderRepository proxy;

        RecordingRepository(Order order) {
            this.order = order;
            this.proxy = (OrderRepository) Proxy.newProxyInstance(
                    OrderRepository.class.getClassLoader(),
                    new Class<?>[]{OrderRepository.class},
                    (object, method, arguments) -> {
                        if (method.getName().equals("findByOrderId")) {
                            return Optional.of(this.order);
                        }
                        if (method.getName().equals("save") || method.getName().equals("saveAndFlush")) {
                            saved = true;
                            return arguments[0];
                        }
                        if (method.getName().equals("toString")) {
                            return "RecordingRepository";
                        }
                        return null;
                    });
        }

        OrderRepository repository() {
            return proxy;
        }
    }

    private static class RecordingRabbitTemplate extends RabbitTemplate {
        private String routingKey;

        @Override
        public void convertAndSend(String exchange, String routingKey, Object message) {
            this.routingKey = routingKey;
        }
    }
}
