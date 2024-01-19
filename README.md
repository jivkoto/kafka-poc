Kafka Poc
==========

This is proof of concept project to get familiar with Kafka raw library and spring boot wrapper library.


Raw Kafka Library
-----
All raw Kafka library code is com.poc.kafka.raw

Kafka Spring Boot
-----
Spring boot related Kafka code is in com.poc.kafka.spring

There are several use cases tested with spring. Each use case sends and receives messages (except load case)
1. Produce and consume String based message. Code is located in StringMessage*. Code for this use case requires manual 
acknowledge after receiving the message
2. Produce and consume Avro based messages that use custom serializer and deserializer that doesn't rely on schema 
 registry AgentStateMessage*
3. Produce and consume Avro based messages that use Confluent's Avro serializer and deserializer that rely on schema 
registry. Note that Schema registry service should be present and configured for this use case to run. Message listener
overrides some of the configurations coming from the container factory. Code is located in AgentStateRepoMessage*
4. Setup to produce load test on specific consumer. There is a separate configuration for this case to be started. This 
is the only case that supports SSL encryption. Code is located in AgentStateLoadMessageSender.

Avro
-----
Avro message's class is created from Avro schema located in /src/main/resources/avro/AgentState.avsc

Configuration
-----
```
#Configures bootstrap server
config.kafka.bootstrapServers=localhost:9092

#Configures schema registry url for use case 4
config.kafka.schema-registry-url=http://localhost:8081

# Enables load poc and configures some of the aspects of the test
config.kafka.load.enabled=false
config.kafka.load.topic=supernova_agent_state_load
config.kafka.load.replicas=1
config.kafka.load.partitions=1
config.kafka.load.ack=1
config.kafka.load.messages-count=10
config.kafka.load.iterations=5
#config.kafka.security-protocol=SSL
#config.kafka.ssl-trust-store-location=
#config.kafka.ssl-trust-store-password=
#config.kafka.ssl-key-store-location=
#config.kafka.ssl-key-store-password=

# enables the resources for the raw library test
usecase.raw-enabled=true

# enables the resources for ths spring based string message test
usecase.spring-string-enabled=true

# enables the resources for the spring based avro message test without schema repository
usecase.spring-avro-enabled=true

# enables the resource for the spring based avro message test that schema registry
usecase.spring-avro-repo-enabled=true
```

Tests
-----
There are a couple of Spock based unit tests