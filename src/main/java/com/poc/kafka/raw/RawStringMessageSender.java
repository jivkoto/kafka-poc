package com.poc.kafka.raw;

import com.github.javafaker.Faker;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(value =  "usecase.raw-enabled", havingValue = "true", matchIfMissing = true)
public class RawStringMessageSender
{
    private final KafkaConfigProperties configProperties;
    private final KafkaProducer<String, String> stringProducer;
    private final Faker faker = new Faker();

    @Scheduled(fixedDelay = 60_000L)
    public void sendToTopic() {
        String hardcodedKey = "RAW-LOTR";
        String message = faker.hobbit().quote();

        String topic = configProperties.getStatusTopicName();
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>(topic, hardcodedKey, message);
        stringProducer.send(producerRecord);

        log.info("Raw sending key:{}, message:{} to topic:{}", hardcodedKey, message, topic);
    }
}
