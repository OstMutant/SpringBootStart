package org.ost.investigate.springboot.demo.web;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.service.JwtClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class JwtController {
    final JwtClient client;

    public JwtController(JwtClient client) {
        this.client = client;
    }

    @GetMapping("/jwt")
    public Mono<String> jwt() {
        log.debug("jwt");
        return client.authenticateForWebClient();
    }
}
