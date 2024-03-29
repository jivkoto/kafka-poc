package com.poc.kafka.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Basic Kafka configuration
 */
@Getter
//@EnableConfigurationProperties
@ConfigurationProperties("config.kafka")
public class KafkaConfigProperties
{
    public static final String DEFAULT_BOOTSTRAP_SERVERS = "wrong";//"localhost:9092";
    public static final String DEFAULT_SCHEMA_REGISTRY_URL = "localhost:8081";
    public static final String DEFAULT_STATUS_TOPIC_NAME = "supernova_status";
    public static final String DEFAULT_AGENT_STATE_TOPIC_NAME = "supernova_agent_state";
    public static final String DEFAULT_AGENT_STATE_REPO_TOPIC_NAME = "supernova_agent_state_repo";

    private final String bootstrapServers;
    private final String schemaRegistryUrl;
    private final String statusTopicName;
    private final String agentStateTopicName;
    private final String agentStateRepoTopicName;
    private final String securityProtocol;
    private final String sslTrustStoreLocation;
    private final String sslTrustStorePassword;
    private final String sslKeyStoreLocation;
    private final String sslKeyStorePassword;

    public KafkaConfigProperties(String bootstrapServers,
                                 String schemaRegistryUrl,
                                 String statusTopicName,
                                 String agentStateTopicName,
                                 String agentStateRepoTopicName,
                                 String securityProtocol,
                                 String sslTrustStoreLocation,
                                 String sslTrustStorePassword,
                                 String sslKeyStoreLocation,
                                 String sslKeyStorePassword){
        this.bootstrapServers = bootstrapServers != null ? bootstrapServers : DEFAULT_BOOTSTRAP_SERVERS;
        this.schemaRegistryUrl = schemaRegistryUrl != null ? schemaRegistryUrl : DEFAULT_SCHEMA_REGISTRY_URL;
        this.statusTopicName = statusTopicName != null ? statusTopicName : DEFAULT_STATUS_TOPIC_NAME;
        this.agentStateTopicName = agentStateTopicName != null ? agentStateTopicName : DEFAULT_AGENT_STATE_TOPIC_NAME;
        this.agentStateRepoTopicName =
                agentStateRepoTopicName != null ? agentStateRepoTopicName : DEFAULT_AGENT_STATE_REPO_TOPIC_NAME;
        this.securityProtocol = securityProtocol;
        this.sslTrustStoreLocation = sslTrustStoreLocation;
        this.sslTrustStorePassword = sslTrustStorePassword;
        this.sslKeyStoreLocation = sslKeyStoreLocation;
        this.sslKeyStorePassword = sslKeyStorePassword;
    }

}
