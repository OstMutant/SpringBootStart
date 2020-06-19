package org.ost.investigate.springboot.demo.interceptors;

import lombok.RequiredArgsConstructor;
import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;


public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {
    private SalesforceAuthorizationService authorizationService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", authorizationService.getAuthorizationHeader());
        return execution.execute(request, body);
    }
}
