package com.poc.kafka;

import org.springframework.kafka.KafkaException;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

/**
 * Holder for the embedded kafka broker. It will handle initialization of the broker before providing it. It helps
 * to set up the broker without depending on a Junit4 @ClassRule.
 */
public class EmbeddedKafkaHolder
{
    private static final EmbeddedKafkaBroker embeddedKafka = new EmbeddedKafkaZKBroker(1);

    private static boolean started;

    private EmbeddedKafkaHolder(){
    }

    public static EmbeddedKafkaBroker getEmbeddedKafka() {
        if (!started){

            try
            {
                // initialize
                embeddedKafka.afterPropertiesSet();
            } catch (Exception e)
            {
                throw new KafkaException("Embedded broker failed to start!", e);
            }
            started = true;
        }
        return embeddedKafka;
    }
}
