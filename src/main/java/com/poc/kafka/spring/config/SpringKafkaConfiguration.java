package com.poc.kafka.spring.config;

import com.poc.kafka.avro.AgentState;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import com.poc.kafka.config.properties.LoadTestConfigProperties;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration that contains all the producers, consumers and factories used for the Spring tests.
 */
@Configuration
public class SpringKafkaConfiguration
{

    public static final String KAFKA_MANUAL_ACK_LISTENER_CONTAINER_FACTORY_NAME = "kafkaManualAckListenerContainerFactory";
    public static final String KAFKA_LISTENER_CONTAINER_FACTORY_NAME = "kafkaListenerContainerFactory";

    // String based use case
    /**
     * Producer factory configured to serialize String messages.
     *
     * @param configProperties - configuration properties
     * @return ProducerFactory
     */
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

    /**
     * KafkaTemplate using string based producer factory
     *
     * @param stringProducerFactory - producer factory
     * @return KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, String> kafkaStringTemplate(ProducerFactory<String, String> stringProducerFactory) {
        return new KafkaTemplate<>(stringProducerFactory);
    }

    // Avro use case

    /**
     * Producer factory configured to serialize AgentState Avro messages.
     *
     * @param configProperties - configuration properties
     * @return ProducerFactory
     */
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

    /**
     * KafkaTemplate using AgentState Avro based producer factory
     *
     * @param agentStateProducerFactory - producer factory
     * @return KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, AgentState> kafkaAgentStateTemplate(ProducerFactory<String, AgentState> agentStateProducerFactory) {
        return new KafkaTemplate<>(agentStateProducerFactory);
    }

    // repo configuration

    /**
     * Producer factory configured to serialize AgentState Avro messages using Confluent schema registry and serializer.
     *
     * @param configProperties - configuration properties
     * @return ProducerFactory
     */
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

    /**
     * KafkaTemplate using AgentState Avro based producer factory
     *
     * @param agentStateRepoProducerFactory - producer factory
     * @return KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, AgentState> kafkaAgentStateRepoTemplate(ProducerFactory<String, AgentState> agentStateRepoProducerFactory) {
        return new KafkaTemplate<>(agentStateRepoProducerFactory);
    }


    // load configuration
    /**
     * Producer factory configured to serialize AgentState Avro messages. Note that it applies load testing
     * configurations. It also supports ssl communication with the brokers
     *
     * @param configProperties - configuration properties
     * @return ProducerFactory
     */
    @Bean("agentStateLoadProducerFactory")
    public ProducerFactory<String, AgentState> agentStateLoadProducerFactory(KafkaConfigProperties configProperties,
                                                                             LoadTestConfigProperties loadProperties){
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers());
        configMap.put(ProducerConfig.RETRIES_CONFIG, 0);
        configMap.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);

        safePut(configMap, "security.protocol", configProperties.getSecurityProtocol());
        safePut(configMap,"ssl.truststore.location", configProperties.getSslTrustStoreLocation());
        safePut(configMap,"ssl.truststore.password", configProperties.getSslTrustStorePassword());
//        safePut(configMap,"ssl.truststore.type", "JKS");
        safePut(configMap, "ssl.keystore.location", configProperties.getSslKeyStoreLocation());
        safePut(configMap, "ssl.keystore.password", configProperties.getSslKeyStorePassword());
//        safePut(configMap, "ssl.keystore.type", "JKS");
        safePut(configMap, ProducerConfig.ACKS_CONFIG, String.valueOf(loadProperties.getAck()));

        return new DefaultKafkaProducerFactory<>(configMap);

    }

    /**
     * Producer factory configured to serialize AgentState Avro messages using load use case configuration
     *
     * @param agentStateLoadProducerFactory - configuration properties
     * @return ProducerFactory
     */
    @Bean
    public KafkaTemplate<String, AgentState> kafkaAgentStateLoadTemplate(ProducerFactory<String, AgentState> agentStateLoadProducerFactory) {
        return new KafkaTemplate<>(agentStateLoadProducerFactory);
    }

    /**
     * Consumer factory that configures string consumers
     *
     * @param configProperties - configuration properties
     * @return ConsumerFactory
     */
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

    /**
     * Configures string based KafkaListenerContainerFactory that requires manual acknowledgment
     *
     * @param stringConsumerFactory - consumer factory
     * @return KafkaListenerContainerFactory
     */
    @Bean(KAFKA_MANUAL_ACK_LISTENER_CONTAINER_FACTORY_NAME)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaManualAckListenerContainerFactory(ConsumerFactory<String, String> stringConsumerFactory)
    {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        // manual ack
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Consumer factory that configures avro based AgentState consumers
     *
     * @param configProperties - configuration properties
     * @return ConsumerFactory
     */
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

    /**
     * Configures AgentState based KafkaListenerContainerFactory
     *
     * @param agentStateConsumerFactory - consumer factory
     * @return KafkaListenerContainerFactory
     */
    @Bean(KAFKA_LISTENER_CONTAINER_FACTORY_NAME)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, AgentState>>
        kafkaListenerContainerFactory(ConsumerFactory<String, AgentState> agentStateConsumerFactory)
    {
        ConcurrentKafkaListenerContainerFactory<String, AgentState> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(agentStateConsumerFactory);
        return factory;
    }

    public static void safePut(Map<String, Object> map, String key, Object value)
    {
        if (value != null)
        {
            map.put(key, value);
        }
    }

}
