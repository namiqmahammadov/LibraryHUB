package com.namiq.msbook.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "library.events";

    public static final String QUEUE = "user.borrow.queue";

    public static final String BOOK_CREATED = "book.created";

    public static final String BORROW_CREATED = "borrow.created";

    public static final String BORROW_RETURNED = "borrow.returned";

    public static final String BORROW_OVERDUE = "borrow.overdue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
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
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}