package com.poc.kafka.raw

import com.poc.kafka.config.properties.KafkaConfigProperties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import spock.lang.Specification
import spock.lang.Subject

class RawStringMessageSenderTest extends Specification {

    def configProperties = Mock(KafkaConfigProperties)
    def stringProducer = Mock(KafkaProducer<String, String>)

    @Subject
    RawStringMessageSender sender  = new RawStringMessageSender(configProperties, stringProducer)

    def 'validate send to topic'()
    {
        given: 'topic name'
            String topic = "topic"
        when: 'when sending'
            sender.sendToTopic()
        then: 'configuration properties are read and stringProducer is called correctly'
            1 * configProperties.getStatusTopicName() >> topic
            0 * configProperties._
            1 * stringProducer.send({ ProducerRecord<String, String> record ->
                record.topic() == topic
                record.key()
                record.value()
            })
    }
}