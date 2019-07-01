package com.romanpulov.violetnotewss.application.config;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.logging.Logger;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Configuration
public class LoggerConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Logger logger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMethodParameter().getContainingClass().getName());
    }
}
