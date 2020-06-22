package org.ost.investigate.springboot.demo.web;

import org.ost.investigate.springboot.demo.dto.Greeting;
import org.ost.investigate.springboot.demo.service.GreetingClient;
import org.ost.investigate.springboot.demo.service.SalesforceAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GreetingController {
    final SalesforceAuthorizationService salesforceAuthorizationService;

    final GreetingClient greetingClient;

    @Autowired
    public GreetingController(SalesforceAuthorizationService salesforceAuthorizationService,
                              GreetingClient greetingClient) {
        this.salesforceAuthorizationService = salesforceAuthorizationService;
        this.greetingClient = greetingClient;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(String.format("Hello, %s!", name));
    }

    @GetMapping("/greeting/redirect")
    public Mono<Greeting> getRedirectedGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return greetingClient.getRedirectedGreeting(name);
    }


}
