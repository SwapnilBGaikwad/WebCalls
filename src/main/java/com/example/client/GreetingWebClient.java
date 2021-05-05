package com.example.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GreetingWebClient {
    private WebClient webClient;

    public GreetingWebClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("SamplePool")
                .maxConnections(100)
                .maxLifeTime(Duration.ofMinutes(3L))
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider);
        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8080")
                .build();
    }

    public Mono<List<String>> getResult() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        return Flux.fromIterable(list)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(a -> getRes())
                .sequential()
                .collectList();
    }

    public Mono<String> getRes() {
        Mono<ResponseEntity<String>> result = webClient.get()
                .uri("/hello")
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .toEntity(String.class);
        return result.map(HttpEntity::getBody);
    }
}
