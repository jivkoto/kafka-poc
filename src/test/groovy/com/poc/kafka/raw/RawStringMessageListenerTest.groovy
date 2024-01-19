package com.poc.kafka.raw

import com.poc.kafka.config.properties.KafkaConfigProperties
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import spock.lang.Specification

import java.time.Duration


class RawStringMessageListenerTest extends Specification {

    def configProperties = Mock(KafkaConfigProperties)
    def stringConsumer = Mock(KafkaConsumer<String, String>)

    RawStringMessageListener listener = new RawStringMessageListener(configProperties, stringConsumer)

    def 'validate receiving messages'(){
        given: 'topic'
            String topic = "topic"
        and: 'mocked resources for the consumer response'
            ConsumerRecords<String, String> consumerRecords = Mock(ConsumerRecords)
            Iterator<String> recordsIterator = Mock()
        when: 'calling the listener to start consuming'
            new Thread(() -> listener.consume()).start()
            sleep(1000)
            listener.shutdown()
            sleep(1000)
        then: 'configuration properties are correctly read'
            1 * configProperties.getStatusTopicName() >> topic
            0 * configProperties._
        and: 'consumer registers on the configured topic'
            1 * stringConsumer.subscribe([topic])
        and: 'consumer polls for records'
            stringConsumer.poll(Duration.ofMillis(250)) >> consumerRecords
            consumerRecords.iterator() >> recordsIterator
            recordsIterator.hasNext() >> false
    }
}