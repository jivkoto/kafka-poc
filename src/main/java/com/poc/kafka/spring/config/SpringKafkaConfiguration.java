package com.poc.kafka.spring.config;

import com.poc.kafka.avro.AgentState;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import com.poc.kafka.serder.AvroDeserializer;
import com.poc.kafka.serder.AvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

@Configuration
public class SpringKafkaConfiguration
{

    public static final String KAFKA_MANUAL_ACK_LISTENER_CONTAINER_FACTORY_NAME = "kafkaManualAckListenerContainerFactory";
    public static final String KAFKA_LISTENER_CONTAINER_FACTORY_NAME = "kafkaListenerContainerFactory";

    @Bean("stringProducerFactory")
    public ProducerFactory<String, String> stringProducerFactory(KafkaConfigProperties configProperties){
        return new DefaultKafkaProducerFactory<>(
                Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                        ProducerConfig.RETRIES_CONFIG, 0,
                        ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        );
    }

    @Bean
    public KafkaTemplate<String, String> kafkaStringTemplate(ProducerFactory<String, String> stringProducerFactory) {
        return new KafkaTemplate<>(stringProducerFactory);
    }

    @Bean("agentStateProducerFactory")
    public ProducerFactory<String, AgentState> agentStateProducerFactory(KafkaConfigProperties configProperties){
        return new DefaultKafkaProducerFactory<>(
                Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                        ProducerConfig.RETRIES_CONFIG, 0,
                        ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
//                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class,
                        "schema.registry.url","localhost:8080",
                        "auto.register.schemas", false)
        );
    }

    @Bean
    public KafkaTemplate<String, AgentState> kafkaAgentStateTemplate(ProducerFactory<String, AgentState> agentStateProducerFactory) {
        return new KafkaTemplate<>(agentStateProducerFactory);
    }


    @Bean("agentStateRepoProducerFactory")
    public ProducerFactory<String, AgentState> agentStateRepoProducerFactory(KafkaConfigProperties configProperties){
        return new DefaultKafkaProducerFactory<>(
                Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                        ProducerConfig.RETRIES_CONFIG, 0,
                        ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class,
                        "schema.registry.url", configProperties.getSchemaRegistryUrl()
//                        "auto.register.schemas", false
                )
        );
    }

    @Bean
    public KafkaTemplate<String, AgentState> kafkaAgentStateRepoTemplate(ProducerFactory<String, AgentState> agentStateRepoProducerFactory) {
        return new KafkaTemplate<>(agentStateRepoProducerFactory);
    }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory(KafkaConfigProperties configProperties){
        return new DefaultKafkaConsumerFactory<>(
                Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "spring-consumer-group",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false,
                        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
//                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                )
        );
    }

    @Bean(KAFKA_MANUAL_ACK_LISTENER_CONTAINER_FACTORY_NAME)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaManualAckListenerContainerFactory(ConsumerFactory<String, String> stringConsumerFactory)
    {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        // manual ack
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AgentState> agentStateConsumerFactory(KafkaConfigProperties configProperties){
        return new DefaultKafkaConsumerFactory<>(
                Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "spring-agent-state-group",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false,
                        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class,
//                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        "schema.registry.url", configProperties.getSchemaRegistryUrl()
                )
        );
    }

    @Bean(KAFKA_LISTENER_CONTAINER_FACTORY_NAME)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, AgentState>>
        kafkaListenerContainerFactory(ConsumerFactory<String, AgentState> agentStateConsumerFactory)
    {
        ConcurrentKafkaListenerContainerFactory<String, AgentState> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(agentStateConsumerFactory);
        return factory;
    }


}
