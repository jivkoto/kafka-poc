package com.poc.kafka.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class AvroUtil
{
    public static <T extends SpecificRecord> byte[] serializeAvro(T data, Schema schema){
        byte[] bytes = null;
        DatumWriter<T> writer = new SpecificDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try
        {
            DataFileWriter<T> dataFileWriter = new DataFileWriter<>(writer);
            dataFileWriter.create(schema, out);
            dataFileWriter.append(data);
            dataFileWriter.close();
            bytes = out.toByteArray();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return bytes;
    }

    public static <T extends SpecificRecord> T deserializeAvro(byte[] in, Schema schema){
        SpecificDatumReader<T> reader = new SpecificDatumReader<>(schema);
        try
        {
            SeekableByteArrayInput sbai = new SeekableByteArrayInput(in);
            try(DataFileStream<T> dataFileStream = new DataFileStream<>(sbai, reader)){
                return dataFileStream.next();
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Schema getSchemaFromBytes(byte[] bytes) {
        try
        {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            DataFileStream<Void> reader = new DataFileStream<>(in, new GenericDatumReader<>());
            return reader.getSchema();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws IOException
//    {
//        SpecificRecordBase agentState = new AgentState("some", "other");
//        byte[] bytes = serializeAvro(agentState, agentState.getSchema());
//        Schema schema = getSchemaFromBytes(bytes);
//        AgentState agentStateDes = (AgentState)deserializeAvro(bytes, schema);
//        log.info("bytes: {}", bytes);
//        log.info("schema: {}", schema);
//        log.info("Agent: {}", agentStateDes);
//    }
}
