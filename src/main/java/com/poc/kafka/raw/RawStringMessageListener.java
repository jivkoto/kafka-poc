package com.poc.kafka.raw;

import com.poc.kafka.config.properties.KafkaConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * Starting raw Kafka message string listener.
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(value =  "usecase.raw-enabled", havingValue = "true", matchIfMissing = true)
public class RawStringMessageListener
{
    private volatile boolean keepConsuming = true;

    private final KafkaConfigProperties configProperties;
    private final KafkaConsumer<String, String> stringConsumer;

    @PostConstruct
    public void init(){
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown(){
        log.info("Shutting down the raw string listener");
        keepConsuming = false;
    }

    /**
     * Starts to listen when spring application context is started
     */
    @EventListener(ContextStartedEvent.class)
    public void startListening(){
        log.info("Start raw listener");
        consume();
    }

    public void consume(){
        stringConsumer.subscribe(List.of(configProperties.getStatusTopicName()));
        while(keepConsuming){
            ConsumerRecords<String, String> consumerRecords = stringConsumer.poll(Duration.ofMillis(250));
            for(ConsumerRecord<String, String> consumerRecord: consumerRecords){
                String key = consumerRecord.key();
                String value = consumerRecord.value();
                String topic = consumerRecord.topic();
                log.info("Raw received key:{}, message:{} to topic:{}", key, value, topic);
            }
        }
    }

}
