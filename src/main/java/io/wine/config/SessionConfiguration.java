package io.wine.config;

import io.wine.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SessionConfiguration {

    @Bean(name = "sessionMap")
    @Qualifier(value = "sessionMap")
    public ConcurrentHashMap<String, User> createSessionMap() {
        return new ConcurrentHashMap<>();
    }

    @Scheduled(fixedDelay = 50000L)
    public void cleanSession(@Qualifier("sessionMap") ConcurrentHashMap<String, User> sessionMap) {
        sessionMap.clear();
    }
}
