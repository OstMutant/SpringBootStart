package org.ost.investigate.springboot.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Optional.ofNullable;

@Service
public class SalesforceAuthorizationService {
    private final AuthorizationConfig authorizationConfig;
    private AuthorizationResponse authorizationResponse;
    private final ReentrantLock reLock = new ReentrantLock(true);

    public SalesforceAuthorizationService(@Autowired AuthorizationConfig authorizationConfig) {
        this.authorizationConfig = authorizationConfig;
    }

    public String getAuthorizationHeader() {
        return ofNullable(authorizationResponse).map(v -> getAuthorizationValue(authorizationResponse)).orElse(authenticate());
    }

    private String getAuthorizationValue(AuthorizationResponse authorizationResponse) {
        return authorizationResponse.getAccessToken();
    }

    private String authenticate() {
        reLock.lock();
        if (this.authorizationResponse != null)
            return getAuthorizationValue(this.authorizationResponse);

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

        authorizationResponse = rest.postForObject(authorizationConfig.getEndpoint(), httpEntity, AuthorizationResponse.class);
        reLock.unlock();
        return getAuthorizationValue(authorizationResponse);
    }

}
