package com.poc.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.poc.kafka")
@SpringBootApplication
public class KafkaPocApplication  implements CommandLineRunner
{

    public static void main(String[] args)
    {
        SpringApplication.run(KafkaPocApplication.class, args).start();
    }

    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void run(String... args) throws Exception {

        String[] beans = applicationContext.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println(bean);
        }
    }

}
