package com.merchant.checkout.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String ORDER_EXCHANGE = "checkout.orders";
    public static final String ORDER_CREATED_QUEUE = "checkout.order-created";
    public static final String CONFIRMATION_QUEUE = "checkout.confirmations";
    public static final String ORDER_CREATED_KEY = "order.created";
    public static final String CONFIRMATION_KEY = "order.confirmation";

    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    Queue confirmationQueue() {
        return new Queue(CONFIRMATION_QUEUE, true);
    }

    @Bean
    Binding orderCreatedBinding(Queue orderCreatedQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(orderExchange).with(ORDER_CREATED_KEY);
    }

    @Bean
    Binding confirmationBinding(Queue confirmationQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(confirmationQueue).to(orderExchange).with(CONFIRMATION_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
