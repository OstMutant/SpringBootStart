package org.ost.investigate.springboot.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ost.investigate.springboot.demo.config.LocalConfig;
import org.ost.investigate.springboot.demo.dto.Greeting;
import org.ost.investigate.springboot.demo.service.GreetingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GreetingClientTests {

    @Autowired
    GreetingClient greetingClient;
    @Autowired
    LocalConfig localConfig;

    private WireMockServer WIREMOCK_SERVER;

    @Before
    public void setUp() {
        WIREMOCK_SERVER = new WireMockServer(localConfig.getPort());
        WIREMOCK_SERVER.start();
    }


    @After
    public void tearDown() {
        WIREMOCK_SERVER.shutdown();
    }

    @Test
    public void getGreeting() throws Exception {
        WIREMOCK_SERVER.stubFor(WireMock.get(WireMock.urlMatching("/greeting\\?name=Test"))
//                .withHeader("Accept", WireMock.equalTo("application/json"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "    \"content\": \"Hello, Test!\"" +
                                "}")));

        Mono<Greeting> result = greetingClient.getRedirectedGreeting("Test");
        System.out.println("result - " + result);
        StepVerifier.create(result)
                .expectNextMatches(v -> v.getContent().equals("Hello, Test!"))
                .verifyComplete();
    }

}
