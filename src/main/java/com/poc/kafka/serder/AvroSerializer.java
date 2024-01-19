package com.poc.kafka.serder;

import com.poc.kafka.avro.AvroUtil;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Avro serializer that doesn't use schema registry
 */
public class AvroSerializer implements Serializer<SpecificRecord>
{
    @Override
    public byte[] serialize(String s, SpecificRecord specificRecord)
    {
        return AvroUtil.serializeAvro(specificRecord, specificRecord.getSchema());
    }
}
