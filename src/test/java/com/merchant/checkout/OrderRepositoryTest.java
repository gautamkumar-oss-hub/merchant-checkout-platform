package com.merchant.checkout;

import com.merchant.checkout.model.Order;
import com.merchant.checkout.model.OrderItem;
import com.merchant.checkout.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void persistsOrderLinesWithOrder() {
        Order order = new Order("ORD-TEST-1", "customer-1", "PENDING");
        order.setItems(java.util.List.of(new OrderItem("product-1", 2), new OrderItem("product-2", 1)));

        orderRepository.saveAndFlush(order);
        Order stored = orderRepository.findByOrderId("ORD-TEST-1").orElseThrow();

        assertEquals("customer-1", stored.getCustomerId());
        assertEquals("PENDING", stored.getStatus());
        assertEquals(2, stored.getItems().size());
        assertTrue(stored.getItems().stream().anyMatch(item -> "product-1".equals(item.getProductId())));
    }
}
