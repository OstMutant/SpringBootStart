package org.ost.investigate.springboot.demo.standalone;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ost.investigate.springboot.demo.config.SalesforceConfig;
import org.ost.investigate.springboot.demo.service.SalesforceSimpleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class SalesforceTest {

    @Autowired
    SalesforceSimpleClient client;

    @Autowired
    SalesforceConfig config;

    @Test
    public void trySalesforce(){

        Mono<JsonNode> result = client.applyQuery("select Id, Name from Account");
        StepVerifier.create(result)
                .expectNextMatches(v -> {
                    System.out.println("!" + v.toString());
                    return true;
                }).verifyComplete();
    }
}
