package com.poc.kafka.serder

import com.poc.kafka.avro.AgentState
import com.poc.kafka.avro.AvroUtil
import spock.lang.Specification
import spock.lang.Subject


class AvroDeserializerTest extends Specification {

    @Subject
    AvroDeserializer deserializer = new AvroDeserializer()

    def 'validate serializer'(){
        given: 'avro to serialize'
            AgentState agentState = new AgentState("id", "Pesho")
            byte[] bytes = AvroUtil.serializeAvro(agentState, agentState.getSchema())
        when:
            AgentState deserializedAgentState = (AgentState)deserializer.deserialize("topic", bytes)
        then:
            agentState == deserializedAgentState
    }
}