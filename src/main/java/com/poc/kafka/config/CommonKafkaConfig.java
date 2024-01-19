package com.poc.kafka.config;

import com.poc.kafka.config.properties.KafkaConfigProperties;
import com.poc.kafka.config.properties.LoadTestConfigProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration that initializes all the topics required for spring and raw based tests
 */
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
    @ConditionalOnProperty(value =  "usecase.spring-avro-enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic agentStateTopic(KafkaConfigProperties configProperties){
        return TopicBuilder.name(configProperties.getAgentStateTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @ConditionalOnProperty(value =  "usecase.spring-avro-repo-enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic agentStateRepoTopic(KafkaConfigProperties configProperties){
        return TopicBuilder.name(configProperties.getAgentStateRepoTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @ConditionalOnProperty(value =  "config.kafka.load.enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic agentStateLoadTopic(LoadTestConfigProperties configProperties){
        return TopicBuilder.name(configProperties.getTopic())
                .partitions(configProperties.getPartitions())
                .replicas(configProperties.getReplicas())
                .build();
    }
}
