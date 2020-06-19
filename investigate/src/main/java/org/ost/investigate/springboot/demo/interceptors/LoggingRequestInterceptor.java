package org.ost.investigate.springboot.demo.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import static java.util.Optional.ofNullable;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        log(request, body, response);
        return response;
    }

    private void log(final HttpRequest request, final byte[] body, final ClientHttpResponse response) throws IOException {
        logger.info("URI: " + request.getURI());
        logger.info("Method: " + request.getMethod());
        logger.info("Request Body: " + ofNullable(body).map(v -> new String(body, Charset.defaultCharset())));
        logger.info("response status code: " + response.getStatusCode());
        logger.info("response status text: " + response.getStatusText());
        logger.info("response body : " + StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
    }
}
