package com.poc.kafka.raw.config;

import com.poc.kafka.config.properties.KafkaConfigProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RawKafkaConfiguration
{

    @Bean(destroyMethod = "close")
    public KafkaProducer<String, String> createStringProducer(KafkaConfigProperties configProperties){
        Map<String, Object> config =
                Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                ProducerConfig.RETRIES_CONFIG, 0,
                ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer<>(config);
    }

    @Bean(destroyMethod = "close")
    public KafkaConsumer<String, String> createStringConsumer(KafkaConfigProperties configProperties){
        Map<String, Object> config =
                Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, "raw-consumer-group",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true,
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
        return new KafkaConsumer<>(config);
    }
}
