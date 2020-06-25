package org.ost.investigate.springboot.demo.web;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.ost.investigate.springboot.demo.service.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class SalesforceController {

    final SalesforceAuthorizationService salesforceAuthorizationService;

    final SalesforceClient salesforceClient;

    @Autowired
    public SalesforceController(SalesforceAuthorizationService salesforceAuthorizationService,
                                SalesforceClient salesforceClient) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
        this.salesforceClient = salesforceClient;
    }

    @GetMapping("/salesforce")
    public Mono<JsonNode> salesforce(@RequestParam(value = "query") String query) {
        log.debug("query({})", query);
        return salesforceClient.applyQuery(query);
    }
}
