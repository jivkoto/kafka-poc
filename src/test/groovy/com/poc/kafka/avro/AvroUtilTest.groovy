package com.poc.kafka.avro

import org.apache.avro.Schema
import spock.lang.Specification
import spock.lang.Subject

@Subject(AvroUtil)
class AvroUtilTest extends Specification {

    def 'serialize and deserialize test'()
    {
        given: 'avro to serialize'
            AgentState agentState = new AgentState("id", "Pesho")
        when: 'serializing the avro'
            byte[] serializedBytes = AvroUtil.serializeAvro(agentState, agentState.getSchema())
        then: 'we expect successful serialization'
            serializedBytes
        when: 'whe try to extract the schema from bytes and deserialize'
            Schema schema = AvroUtil.getSchemaFromBytes(serializedBytes)
            AgentState deserialized = AvroUtil.deserializeAvro(serializedBytes, schema)
        then: 'deserialized object is as the original one'
            agentState == deserialized
    }

}