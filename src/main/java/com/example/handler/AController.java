package com.example.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class AController {
    @Autowired
    private GreetingHandler greetingHandler;

    @GetMapping("/hello")
    public Mono<ResponseEntity<String>> hello() {
        return greetingHandler.hello()
                .map(s -> ResponseEntity.status(HttpStatus.CREATED).body("successful"))
                .doOnSuccess(s -> {
                    System.out.println("s " + s);
                });
    }


    @GetMapping("/test")
    public Mono<ResponseEntity<String>> test() {
        return greetingHandler.testServer()
                .map(s -> ResponseEntity.status(HttpStatus.CREATED).body("successful"))
                .doOnSuccess(s -> {
                    System.out.println("s " + s);
                });
    }

}
