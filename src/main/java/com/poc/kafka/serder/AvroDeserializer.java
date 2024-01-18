package com.poc.kafka.serder;

import com.poc.kafka.avro.AvroUtil;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Deserializer;

public class AvroDeserializer implements Deserializer<SpecificRecord>
{
    @Override
    public SpecificRecord deserialize(String s, byte[] bytes)
    {
        Schema schema = AvroUtil.getSchemaFromBytes(bytes);
        return AvroUtil.deserializeAvro(bytes, schema);
    }
}
