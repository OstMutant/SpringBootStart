package org.ost.investigate.springboot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.config.LocalConfig;
import org.ost.investigate.springboot.demo.dto.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GreetingClient {
    private final WebClient webClient;

    public GreetingClient(LocalConfig config) {
        this.webClient = WebClient.builder()
                .baseUrl(String.format("http://%s:%s", config.getHost(), config.getPort()))
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

    public Mono<Greeting> getRedirectedTest(String name) {
//        throw new RuntimeException();
        return webClient.get()
                .uri("/greeting1?name={name}", name)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    return Mono.error(new Exception("Test"));
                })
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
