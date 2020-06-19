package org.ost.investigate.springboot.demo.web;

import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    final SalesforceAuthorizationService salesforceAuthorizationService;

    public GreetingController(@Autowired SalesforceAuthorizationService salesforceAuthorizationService) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        salesforceAuthorizationService.getAuthorizationHeader();
        return new Greeting(String.format("Hello, %s!", name));
    }
}
