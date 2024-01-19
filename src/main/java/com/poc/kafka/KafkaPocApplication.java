package com.poc.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.poc.kafka.config.properties")
@SpringBootApplication
public class KafkaPocApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(KafkaPocApplication.class, args).start();
    }
}
