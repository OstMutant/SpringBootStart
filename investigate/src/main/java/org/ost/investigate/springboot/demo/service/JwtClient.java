package org.ost.investigate.springboot.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.config.AuthorizationConfig;
import org.ost.investigate.springboot.demo.config.SalesforceConfig;
import org.ost.investigate.springboot.demo.service.components.KeyRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class JwtClient {

    private final AuthorizationConfig authorizationConfig;
    private final SalesforceConfig config;
    final KeyRetriever keyRetriever;

    @Autowired
    public JwtClient(AuthorizationConfig authorizationConfig, SalesforceConfig config, KeyRetriever keyRetriever) {
        this.authorizationConfig = authorizationConfig;
        this.config = config;
        this.keyRetriever = keyRetriever;
    }

    private String createJwtToken() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);
        String jwt = Jwts.builder()
                .setIssuer(authorizationConfig.getClientId())
                .setAudience("https://login.salesforce.com")
                .setSubject(authorizationConfig.getUsername())
                .setExpiration(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.RS256, keyRetriever.getKey())
                .compact();
        log.debug("jwt - " + jwt);
        return jwt;
    }

    public Mono<String> authenticateForWebClient() {
        log.debug("authenticateForWebClient");
        WebClient webClient = WebClient.builder()
                .baseUrl("https://login.salesforce.com")
                .defaultHeaders(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .build();
        return webClient.post()
                .bodyValue(getAuthorizationRequest())
                .retrieve().bodyToMono(String.class);
    }

    private MultiValueMap<String, String> getAuthorizationRequest() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
        map.add("assertion", createJwtToken());

        map.add("client_id", authorizationConfig.getClientId());
        map.add("client_secret", authorizationConfig.getClientSecret());
        map.add("redirect_uri", "https://openidconnect.herokuapp.com/callback");
        return map;
    }
}
