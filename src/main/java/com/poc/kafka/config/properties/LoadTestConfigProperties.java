package com.poc.kafka.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the load test
 */
@Getter
@AllArgsConstructor
@ConfigurationProperties("config.kafka.load")
public class LoadTestConfigProperties
{
    public static final String DEFAULT_AGENT_STATE_LOAD_TOPIC_NAME = "supernova_agent_state_load";
    public static final int DEFAULT_TOPIC_REPLICAS = 3;
    public static final int DEFAULT_TOPIC_PARTITIONS = 3;
    public static final int DEFAULT_ACK = 1;
    public static final int DEFAULT_MESSAGES_COUNT = 1;
    public static final int DEFAULT_ITERATIONS = 1;

    private boolean enabled = false;
    private String topic = DEFAULT_AGENT_STATE_LOAD_TOPIC_NAME;
    private int replicas = DEFAULT_TOPIC_REPLICAS;
    private int partitions = DEFAULT_TOPIC_PARTITIONS;
    private int ack = DEFAULT_ACK;
    private int messagesCount = DEFAULT_MESSAGES_COUNT;
    private int iterations = DEFAULT_ITERATIONS;

}
