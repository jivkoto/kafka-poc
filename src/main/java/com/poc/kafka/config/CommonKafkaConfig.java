package com.poc.kafka.config;

import com.poc.kafka.config.properties.KafkaConfigProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class CommonKafkaConfig
{

    @Bean
    public NewTopic statusTopic(KafkaConfigProperties configProperties){
        return TopicBuilder.name(configProperties.getStatusTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic agentStateTopic(KafkaConfigProperties configProperties){
        return TopicBuilder.name(configProperties.getAgentStateTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }

//    @Bean("stringProducerConfigurationMap")
//    @Qualifier("stringProducerConfigurationMap")
//    public Map<String, Object> stringProducerConfigurationMap(KafkaConfigProperties configProperties){
//        return Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
//                ProducerConfig.RETRIES_CONFIG, 0,
//                ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432,
//                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
//                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//    }
//
//    @Bean
//    public Map<String, Object> stringConsumerConfigurationMap(KafkaConfigProperties configProperties){
//        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers(),
//                ConsumerConfig.GROUP_ID_CONFIG, "spring-consumer-group",
//                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false,
//                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000,
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
////                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest",
//                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
//        );
//    }
}
