package org.ost.investigate.springboot.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.ost.investigate.springboot.demo.config.SalesforceConfig;
import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.ost.investigate.springboot.demo.service.SalesforceSimpleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
public class SalesforceTest {

    @Autowired
    SalesforceSimpleClient client;

    @Autowired
    SalesforceConfig config;

    private WireMockServer WIREMOCK_SERVER;

    @Autowired
    private SalesforceAuthorizationService salesforceAuthorizationService;

//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        WIREMOCK_SERVER = new WireMockServer(config.getPort());
//        WIREMOCK_SERVER.start();
//    }
//
//
//    @After
//    public void tearDown() {
//        WIREMOCK_SERVER.shutdown();
//    }

    @Test
    public void trySalesforce() throws Exception {
        WIREMOCK_SERVER.stubFor(WireMock.post(WireMock.urlMatching("/services/oauth2/token"))
//                .withHeader("Accept", WireMock.equalTo("application/json"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"access_token\": \"123\",\n" +
                                "    \"instance_url\": \"https://creative-koala-kuj6mn-dev-ed.my.salesforce.com\",\n" +
                                "    \"id\": \"https://login.salesforce.com/id/00D3h000002uMxmEAE/0053h0000021UQFAA2\",\n" +
                                "    \"token_type\": \"Bearer\",\n" +
                                "    \"issued_at\": \"1593101315545\",\n" +
                                "    \"signature\": \"japByn9hoj+Fx/K6uqRs/5R22PWDPbCCpdtFRoKRCeQ=\"\n" +
                                "}")));

        WIREMOCK_SERVER.stubFor(WireMock.get(WireMock.urlMatching("/services/data/v48.0/query\\?q=test"))
//                .withHeader("Accept", WireMock.equalTo("application/json"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Authorization", "Bearer 123")
                        .withBody("{\n" +
                                "    \"totalSize\": 13,\n" +
                                "    \"done\": true,\n" +
                                "    \"records\": [\n" +
                                "        {\n" +
                                "            \"attributes\": {\n" +
                                "                \"type\": \"Account\",\n" +
                                "                \"url\": \"/services/data/v48.0/sobjects/Account/0012X0000219mjhQAA\"\n" +
                                "            },\n" +
                                "            \"Id\": \"0012X0000219mjhQAA\",\n" +
                                "            \"Name\": \"Test\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")));


        Mono<JsonNode> result = client.applyQuery("test");
        StepVerifier.create(result)
                .expectNextMatches(v -> {
                    System.out.println("!" + v.toString());
                    return true;
                })
                .verifyComplete();
    }
}
