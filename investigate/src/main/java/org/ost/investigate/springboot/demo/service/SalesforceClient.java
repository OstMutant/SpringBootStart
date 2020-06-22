package org.ost.investigate.springboot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SalesforceClient {
    private final WebClient webClient;

    private final SalesforceAuthorizationService salesforceAuthorizationService;

    public SalesforceClient(SalesforceAuthorizationService salesforceAuthorizationService) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public Mono<String> getSalesforceGreeting(String name) {
        return webClient.get()
                .uri("/greeting?name={name}", name)
                .headers(h -> h.setBearerAuth(salesforceAuthorizationService.getAuthorizationHeader()))
                .retrieve()
                .bodyToMono(String.class);
    }
}
