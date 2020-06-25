package org.ost.investigate.springboot.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.config.SalesforceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SalesforceSimpleClient {
    private final WebClient webClient;

    private final SalesforceAuthorizationService salesforceAuthorizationService;

    @Autowired
    public SalesforceSimpleClient(SalesforceAuthorizationService salesforceAuthorizationService, SalesforceConfig config) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
        this.webClient = WebClient.builder()
                .baseUrl(config.getHost())
                .build();
    }

    public Mono<JsonNode> applyQuery(String query) {
        return webClient.get()
                .uri("/services/data/v48.0/query?q={query}", query)
                .headers(h -> h.setBearerAuth(salesforceAuthorizationService.getAuthorizationHeader()))
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
