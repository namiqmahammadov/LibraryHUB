package com.namiq.msuser.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "library.events";

    public static final String QUEUE = "user.borrow.queue";

    public static final String DLX = "library.events.dlx";
    public static final String DLQ = "user.borrow.dlq";
    public static final String DLQ_ROUTING_KEY = "user.borrow.dlq";

    public static final String BOOK_CREATED = "book.created";
    public static final String BORROW_CREATED = "borrow.created";
    public static final String BORROW_RETURNED = "borrow.returned";
    public static final String BORROW_OVERDUE = "borrow.overdue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Binding bookCreatedBinding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(BOOK_CREATED);
    }

    @Bean
    public Binding borrowCreatedBinding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(BORROW_CREATED);
    }

    @Bean
    public Binding borrowReturnedBinding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(BORROW_RETURNED);
    }

    @Bean
    public Binding borrowOverdueBinding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(BORROW_OVERDUE);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}