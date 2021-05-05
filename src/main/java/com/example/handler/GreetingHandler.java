package com.example.handler;

import com.example.client.GreetingWebClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {
    private GreetingWebClient gwc = new GreetingWebClient();

    public Mono<ServerResponse> hello() {
//        for (int i = 0; i < 100000; i++) {
//        }
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Hello, Spring!"));
    }


    public Mono<ResponseEntity<String>> testServer() {
        return gwc.getResult()
                .map(s -> ResponseEntity.status(HttpStatus.CREATED).body("successful"))
                .doOnSuccess(s -> System.out.println("Success"));
    }

}
