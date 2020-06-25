package org.ost.investigate.springboot.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.config.AuthorizationConfig;
import org.ost.investigate.springboot.demo.dto.authentication.AuthorizationRequest;
import org.ost.investigate.springboot.demo.dto.authentication.AuthorizationResponse;
import org.ost.investigate.springboot.demo.interceptors.LoggingRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class SalesforceAuthorizationService {
    private final AuthorizationConfig authorizationConfig;
    private AtomicReference<String> token = new AtomicReference<>();

    @Autowired
    public SalesforceAuthorizationService(AuthorizationConfig authorizationConfig) {
        this.authorizationConfig = authorizationConfig;
    }

    public synchronized String getAuthorizationHeader() {
        log.debug("get token - " + token.get());
        String currentToken = token.get();
        return currentToken != null ? currentToken : authenticate();
//        return ofNullable(token.get()).orElse(authenticate());
    }

    private String authenticate() {
        log.debug("authenticate");
        AuthorizationRequest authorizationRequest = AuthorizationRequest.builder()
                .clientId(authorizationConfig.getClientId())
                .clientSecret(authorizationConfig.getClientSecret())
                .username(authorizationConfig.getUsername())
                .password(authorizationConfig.getPassword())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.setAll(new ObjectMapper().convertValue(authorizationRequest, new TypeReference<>() {
        }));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
        requestFactory.setOutputStreaming(false);

        RestTemplate rest = new RestTemplate();
        rest.setInterceptors(interceptors);
        rest.setRequestFactory(bufferingClientHttpRequestFactory);
        token.set(Objects.requireNonNull(rest.postForObject(authorizationConfig.getEndpoint(), httpEntity, AuthorizationResponse.class)).getAccessToken());
        return token.get();
    }

}
