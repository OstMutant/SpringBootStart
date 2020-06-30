package org.ost.investigate.springboot.demo.web;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.dto.AccountRequest;
import org.ost.investigate.springboot.demo.dto.AccountResponse;
import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.ost.investigate.springboot.demo.service.SalesforceSimpleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class SalesforceController {

    final SalesforceAuthorizationService salesforceAuthorizationService;

    final SalesforceSimpleClient salesforceClient;

    @Autowired
    public SalesforceController(SalesforceAuthorizationService salesforceAuthorizationService,
                                SalesforceSimpleClient salesforceClient) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
        this.salesforceClient = salesforceClient;
    }

    @GetMapping("/salesforce")
    public Mono<JsonNode> salesforce(@RequestParam(value = "query") String query) {
        log.debug("query({})", query);
        return salesforceClient.applyQuery(query);
    }

    @PostMapping("/account")
    public Mono<AccountResponse> salesforce(@RequestBody AccountRequest account) {
        log.debug("account - {}", account.toString());
        return salesforceClient.createAccount(account);
    }

}
