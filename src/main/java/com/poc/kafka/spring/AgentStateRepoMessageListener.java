package com.poc.kafka.spring;

import com.poc.kafka.avro.AgentState;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import com.poc.kafka.spring.config.SpringKafkaConfiguration;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value =  "usecase.spring-avro-repo-enabled", havingValue = "true", matchIfMissing = true)
public class AgentStateRepoMessageListener
{
    @KafkaListener(containerFactory = SpringKafkaConfiguration.KAFKA_LISTENER_CONTAINER_FACTORY_NAME,
        topics = KafkaConfigProperties.DEFAULT_AGENT_STATE_REPO_TOPIC_NAME,
        properties = {
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG + "=io.confluent.kafka.serializers.KafkaAvroDeserializer",
                KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG + "=true"}
    )
    public void consumeAgentStateMessage(ConsumerRecord<String, AgentState> consumerRecord){
        String key = consumerRecord.key();
        AgentState value = consumerRecord.value();

        String topic = consumerRecord.topic();
        log.info("<r-- Spring agent state received key:{}, message:{} to topic:{}", key, value, topic);
    }
}
