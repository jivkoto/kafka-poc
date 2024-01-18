package com.poc.kafka.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;


@Getter @Setter
@ConfigurationProperties("config.kafka")
@Component
public class TestProperties
{
    public static final String DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String DEFAULT_SCHEMA_REGISTRY_URL = "localhost:8081";
    public static final String DEFAULT_STATUS_TOPIC_NAME = "supernova_status";
    public static final String DEFAULT_AGENT_STATE_TOPIC_NAME = "supernova_agent_state";
    public static final String DEFAULT_AGENT_STATE_REPO_TOPIC_NAME = "supernova_agent_state_repo";

    private String bootstrapServers;
    private  String schemaRegistryUrl;
    private  String statusTopicName;
    private  String agentStateTopicName;
    private  String agentStateRepoTopicName;

}
