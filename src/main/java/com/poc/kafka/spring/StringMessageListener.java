package com.poc.kafka.spring;

import com.poc.kafka.spring.config.SpringKafkaConfiguration;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value =  "usecase.spring-string-enabled", havingValue = "true", matchIfMissing = true)
public class StringMessageListener
{
    @KafkaListener(containerFactory = SpringKafkaConfiguration.KAFKA_MANUAL_ACK_LISTENER_CONTAINER_FACTORY_NAME,
            topics = KafkaConfigProperties.DEFAULT_STATUS_TOPIC_NAME)
    public void consumeStringMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack)
    {
        String key = consumerRecord.key();
        String value = consumerRecord.value();
        String topic = consumerRecord.topic();
        log.info("<s-- Spring received key:{}, message:{} to topic:{}", key, value, topic);

        // manual acknowledgment
        ack.acknowledge();
    }
}
