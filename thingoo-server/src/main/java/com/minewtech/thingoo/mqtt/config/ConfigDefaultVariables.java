package com.minewtech.thingoo.mqtt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.minewtech.thingoo"})

public class ConfigDefaultVariables {
/**
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() throws Exception {
        placeholderConfigurer().setIgnoreUnresolvablePlaceholders(true);
        placeholderConfigurer().setIgnoreResourceNotFound(true);
        return new PropertySourcesPlaceholderConfigurer();
    }
    */
}