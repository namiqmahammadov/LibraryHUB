package com.namiq.msbook.publisher;

import com.namiq.msbook.config.RabbitMQConfig;
import com.namiq.msbook.event.LibraryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String routingKey, LibraryEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                event
        );

    }
}