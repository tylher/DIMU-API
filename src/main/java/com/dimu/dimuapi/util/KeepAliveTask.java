package com.dimu.dimuapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KeepAliveTask {
    private static final Logger logger = LoggerFactory.getLogger(KeepAliveTask.class);
private final WebClient webClient = WebClient.create("http://localhost:8080");

    @Scheduled(fixedRate = 120000) // every 2 minutes
    public void pingSelf() {

            webClient.get().uri("/actuator/health")
                     .retrieve()
                     .bodyToMono(String.class)
                     .doOnNext(response -> logger.info("Keep-alive ping successful: {}", response))
                     .doOnError(error-> logger.warn("Keep-alive ping failed: {}", error.getMessage()))
                     .subscribe();

    }
}
