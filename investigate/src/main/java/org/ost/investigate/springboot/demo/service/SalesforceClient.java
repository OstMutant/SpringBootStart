package org.ost.investigate.springboot.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.config.SalesforceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.Charset.defaultCharset;

@Service
@Slf4j
public class SalesforceClient {
    private final WebClient webClient;

    private final SalesforceAuthorizationService salesforceAuthorizationService;

    public class HttpLoggingHandler extends LoggingHandler {

        @Override
        protected String format(ChannelHandlerContext ctx, String event, Object arg) {
            if (arg instanceof ByteBuf) {
                ByteBuf msg = (ByteBuf) arg;
                return msg.toString(StandardCharsets.UTF_8);
            }
            return super.format(ctx, event, arg);
        }
    }

    @Autowired
    public SalesforceClient(SalesforceAuthorizationService salesforceAuthorizationService, SalesforceConfig config) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(tcpClient ->
                        tcpClient.bootstrap(bootstrap ->
                                BootstrapHandlers.updateLogSupport(bootstrap, new HttpLoggingHandler())));
        this.webClient = WebClient.builder()
                .baseUrl("https://" + config.getAcc() + "." + config.getApp() + ".salesforce.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
