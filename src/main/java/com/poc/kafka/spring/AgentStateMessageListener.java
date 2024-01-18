package com.poc.kafka.spring;

import com.poc.kafka.avro.AgentState;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import com.poc.kafka.spring.config.SpringKafkaConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value =  "usecase.spring-avro-enabled", havingValue = "true", matchIfMissing = true)
public class AgentStateMessageListener
{
    @KafkaListener(containerFactory = SpringKafkaConfiguration.KAFKA_LISTENER_CONTAINER_FACTORY_NAME,
        topics = KafkaConfigProperties.DEFAULT_AGENT_STATE_TOPIC_NAME)
    public void consumeAgentStateMessage(ConsumerRecord<String, AgentState> consumerRecord){
        String key = consumerRecord.key();
        AgentState value = consumerRecord.value();
        String topic = consumerRecord.topic();
        log.info("<a-- Spring agent state received key:{}, message:{} to topic:{}", key, value, topic);
    }
}
