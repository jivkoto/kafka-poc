package com.poc.kafka.spring.load;

import com.github.javafaker.Faker;
import com.poc.kafka.avro.AgentState;
import com.poc.kafka.config.properties.LoadTestConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Scheduled based AgentState Avro message sender that execute load tests that are externally configured.
 * It executes N cycles of M send messages.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value =  "config.kafka.load.enabled", havingValue = "true", matchIfMissing = true)
public class AgentStateLoadMessageSender
{
    /** Holds the executed iterations */
    private final AtomicInteger iterations = new AtomicInteger(0);
    private final KafkaTemplate<String, AgentState> kafkaAgentStateLoadTemplate;
    private final LoadTestConfigProperties  loadConfig;

    private final Faker faker = new Faker();

    /**
     * Sends big load of configured number of messages. Also counts the current iterations. Once all iterations are
     * executed this scheduler has no effect and iteration is discarded.
     */
    @Scheduled(fixedDelay = 1_000L)
    public void sendToTopic(){

        int messageCount = loadConfig.getMessagesCount();
        int requestedIterations = loadConfig.getIterations();
        int passedIterations = iterations.get();
        String topic = loadConfig.getTopic();

        if (requestedIterations > passedIterations)
        {
            StopWatch stopWatch = StopWatch.createStarted();
            for (int i = 0; i < messageCount; i++)
            {
                String id = faker.name().username();
                String state = faker.pokemon().name();

                AgentState agentState = new AgentState(id, state);

                ProducerRecord<String, AgentState> producerRecord = new ProducerRecord<>(topic, id, agentState);
                kafkaAgentStateLoadTemplate.send(producerRecord);

                // TODO remove log for load testing.
//                log.info("--l> Spring agent state sending key:{}, message:{} to topic:{}", id, agentState, topic);
            }
            stopWatch.stop();
            int currentIteration = iterations.addAndGet(1);
            log.info("Iteration:{} sent:{} messages to topic:{} for:{}s",
                    currentIteration, messageCount, topic, stopWatch.getTime(TimeUnit.SECONDS));
        } else {
            log.info("Load testing finished!");
        }
    }
}
