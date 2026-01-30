package com.example.productmicroservice.service;

import com.example.productmicroservice.model.CreateProductRestModel;
import com.example.ws.core.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


//    public String createProduct(CreateProductRestModel productRestModel) {
//        String productId = UUID.randomUUID().toString();
//        // persist product details into database BEFORE PUBLISHING TABLE
//        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productRestModel.getTitle(), productRestModel.getPrice(), productRestModel.getQuantity());
//
//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent);
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                logger.error("Failed to Send Message - " + exception.getMessage());
//            } else {
//                logger.info("Message Sent Sucessfully - " + result.getRecordMetadata());
//
//            }
//        });
//        future.join();
//        logger.info("Returning Product ID");
//        return productId;
//    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {
        String productId = UUID.randomUUID().toString();
        // persist product details into database BEFORE PUBLISHING TABLE
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productRestModel.getTitle(), productRestModel.getPrice(), productRestModel.getQuantity());

        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent).get();

        logger.info(
                "partition - {} " +
                        "topic - {} " +
                        "offset - {} - {} - {}" , result.getRecordMetadata().partition(),result.getRecordMetadata().topic(),result.getRecordMetadata().offset(),result.getRecordMetadata().topic(),result.getRecordMetadata().timestamp());

//
        logger.info("Returning Product ID");
        return productId;
    }


}
