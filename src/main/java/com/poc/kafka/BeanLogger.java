package com.poc.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

/**
 * Helper class that logs all beans in the application context if it is enabled
 */
@Slf4j
@RequiredArgsConstructor
//@Component
public class BeanLogger  implements CommandLineRunner
{
    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args){

        String[] beans = applicationContext.getBeanDefinitionNames();
        for (String bean : beans) {
            log.info("{}", bean);
        }
    }
}
