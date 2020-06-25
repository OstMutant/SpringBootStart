package org.ost.investigate.springboot.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

//@SpringBootTest
class ApplicationTests {
    private AtomicReference<String> token = new AtomicReference<>();
    @Test
    void contextLoads() {
        token.set("123");
        System.out.println("Test - " + ofNullable(token.get()).orElse(""));
    }

}
