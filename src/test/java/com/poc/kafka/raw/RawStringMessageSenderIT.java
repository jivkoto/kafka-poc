package com.poc.kafka.raw;

import com.poc.kafka.BaseKafkaIntegrationTest;
import com.poc.kafka.config.properties.KafkaConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RawStringMessageSenderIT extends BaseKafkaIntegrationTest
{
    @Test
    void rawStringMessageSenderTest(){
        // given
        // configure a consumer that checks that the sender has sent messages on the destination topic
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, String> consumer = cf.createConsumer();

        // when
        // register to consume on the topic that the sender wills send messages
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, KafkaConfigProperties.DEFAULT_STATUS_TOPIC_NAME);

        // then
        // receive messages on this topic
        Awaitility.await().atMost(20, TimeUnit.SECONDS).until(() -> {
            ConsumerRecords<String, String> replies = KafkaTestUtils.getRecords(consumer);
            if (replies.count() > 0){
                for (ConsumerRecord<String, String> record : replies) {
                    log.info("test in -->key:{}, value:{}, topic:{}", record.key(), record.value(), record.topic());
                }
                return true;
            }
            return false;
        });
    }
}
