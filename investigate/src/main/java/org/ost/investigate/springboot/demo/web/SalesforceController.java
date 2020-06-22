package org.ost.investigate.springboot.demo.web;

import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.ost.investigate.springboot.demo.service.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
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
    public Mono<String> salesforce(@RequestParam(value = "name", defaultValue = "World") String name) {
        return salesforceClient.getSalesforceGreeting(name);
    }
}
