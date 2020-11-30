package org.ost.investigate.springboot.demo.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.config.LocalConfig;
import org.ost.investigate.springboot.demo.dto.Greeting;
import org.ost.investigate.springboot.demo.service.exceptions.MyCustomConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

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

    @SneakyThrows
    public String getRedirectedTest(String name) {
        throw new IOException();
//        return webClient.get()
//                .uri("/greeting?name={name}", name)
//                .retrieve()
//                .onStatus(v->v.is2xxSuccessful(), response -> {
//                    return Mono.error(new MyCustomConnectionException(response.statusCode().toString(),"Test Exception"));
//                })
//                .bodyToMono(String.class).block();
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
