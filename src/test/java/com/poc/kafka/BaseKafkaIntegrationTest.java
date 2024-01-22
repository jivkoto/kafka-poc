package com.poc.kafka;

import com.poc.kafka.config.properties.KafkaConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

/**
 * Base integration test that configures and starts the embedded kafka broker and defines the integration tests as
 * spring boot integration tests
 */
@Slf4j
@SpringBootTest
public class BaseKafkaIntegrationTest
{
    /** Embedded kafka broker initialized for all successor tests.*/
    protected static EmbeddedKafkaBroker embeddedKafka;

    @BeforeAll
    public static void setup(){
        embeddedKafka = EmbeddedKafkaHolder.getEmbeddedKafka();
        log.info("port:{}", System.getProperty("spring.embedded.kafka.brokers"));
        String topic = KafkaConfigProperties.DEFAULT_STATUS_TOPIC_NAME;
        embeddedKafka.addTopics(topic);
        log.info("Just created topic:{}", topic);
    }
}
