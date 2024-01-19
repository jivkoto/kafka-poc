package com.poc.kafka.serder

import com.poc.kafka.avro.AgentState
import com.poc.kafka.avro.AvroUtil
import spock.lang.Specification
import spock.lang.Subject


class AvroSerializerTest extends Specification {

    @Subject
    AvroSerializer serializer = new AvroSerializer()

    def 'validate serializer'(){
        given: 'avro to serialize'
            AgentState agentState = new AgentState("id", "Pesho")
        when:
            byte[] actualBytes = serializer.serialize("topic", agentState)
        then:
            agentState == AvroUtil.deserializeAvro(actualBytes, agentState.getSchema())
    }
}