package com.example.ws.emailnotificationservice.handler;

import com.example.ws.core.ProductCreatedEvent;
import com.example.ws.emailnotificationservice.error.NonRetryableException;
import com.example.ws.emailnotificationservice.error.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(id = "product-created-consumer", topics = "product-created-event-topic", containerFactory = "kafkaListenerContainerFactory")
public class ProductCreatedEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductCreatedEventHandler.class);

    RestTemplate restTemplate;

    @KafkaHandler
    public void handle(@Payload ProductCreatedEvent event) {

        logger.info("[ProductCreatedEvent] Received event: {} - {} - {} - {} - {} ", event.getTitle(), event.getProductID(), event.getPrice(), event.getClass(), event.getQuantity());

        try {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8082", HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                logger.info("Received Response from a remote server" + response.getBody());
            }
        } catch (ResourceAccessException e) {
            logger.error(e.getMessage());
            throw new RetryableException(e);
        } catch (HttpServerErrorException e) {
            logger.error(e.getMessage());
            throw new NonRetryableException(e);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NonRetryableException(e);
        }

    }
}
