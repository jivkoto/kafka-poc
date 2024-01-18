package com.poc.kafka.spring;

import com.github.javafaker.Faker;
import com.poc.kafka.avro.AgentState;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value =  "usecase.spring-avro-repo-enabled", havingValue = "true", matchIfMissing = true)
public class AgentStateRepoMessageSender
{
    private final KafkaTemplate<String, AgentState> kafkaAgentStateRepoTemplate;
    private final KafkaConfigProperties configProperties;

    private final Faker faker = new Faker();

    @Scheduled(fixedDelay = 30_000L)
    public void sendToTopic(){
        String id = faker.name().username();
        String state = faker.pokemon().name();

        AgentState agentState = new AgentState(id, state);

        String topic = configProperties.getAgentStateRepoTopicName();
        ProducerRecord<String, AgentState> producerRecord = new ProducerRecord<>(topic, id, agentState);
        kafkaAgentStateRepoTemplate.send(producerRecord);

        log.info("--r> Spring agent state sending key:{}, message:{} to topic:{}", id, agentState, topic);
    }
}
