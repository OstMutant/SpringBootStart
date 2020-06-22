package org.ost.investigate.springboot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.dto.Greeting;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class GreetingClient {
    private final WebClient webClient;

    public GreetingClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }
    public Mono<Greeting> getRedirectedGreeting(String name) {
        return webClient.get()
                .uri("/greeting?name={name}", name)
                .retrieve()
                .bodyToMono(Greeting.class);
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request URI: " + clientRequest.url());
            log.info("Request Method: " + clientRequest.method());
            log.info("Request Headers: " + clientRequest.headers().toString());
//            log.info("Request Body: " + ofNullable(clientRequest.body()).map(v -> new String(body, Charset.defaultCharset())));
            return Mono.just(clientRequest);
        });
    }
    ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status code: " + clientResponse.statusCode());
//            log.info("Response body : " + StreamUtils.copyToString(clientResponse., Charset.defaultCharset()));
            return Mono.just(clientResponse);
        });
    }
}
