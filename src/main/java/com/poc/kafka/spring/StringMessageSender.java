package com.poc.kafka.spring;

import com.github.javafaker.Faker;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Component that sends string Kafka messages based on scheduler. Sender uses Spring's {@link KafkaTemplate}
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(value =  "usecase.spring-string-enabled", havingValue = "true", matchIfMissing = true)
public class StringMessageSender
{
    private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final KafkaConfigProperties configProperties;
    private final Faker faker = new Faker();

    @Scheduled(fixedDelay = 60_000L)
    public void sendToTopic() {
        String hardcodedKey = "LOTR";
        String message = faker.hobbit().quote();

        String topic = configProperties.getStatusTopicName();
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>(topic, hardcodedKey, message);
        kafkaStringTemplate.send(producerRecord);

        log.info("--s> Spring sending key:{}, message:{} to topic:{}", hardcodedKey, message, topic);
    }
}
