package com.example.client;

import com.google.common.collect.Lists;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GreetingWebClient {
    private WebClient webClient;

    public GreetingWebClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("SamplePool")
                .maxConnections(10)
                .maxLifeTime(Duration.ofMinutes(3L))
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider);
        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<String> getResult() {
        System.out.println("Started request " + Thread.currentThread().getName());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        List<String> results = new ArrayList<>();
        List<List<String>> partitions = Lists.partition(list, 5);
//        for (List<String> partition : partitions) {
//            List<Mono<String>> finalList = new ArrayList<>();
//            for (String item : partition) {
//                finalList.add(getRes());
//            }
//            Mono<List<String>> listMono = Flux.merge(finalList).collectList();
//            results.addAll(listMono.block());
//        }
//        System.out.println("Completed request " + Thread.currentThread().getName());
//        return results;

        Mono<List<List<String>>> listMono = Flux.fromIterable(partitions)
                .flatMap(partition -> {
                    List<Mono<String>> finalList = new ArrayList<>();
                    for (String item : partition) {
                        finalList.add(getRes());
                    }
                    return Flux.merge(finalList).collectList();
                })
                .collectList();
        List<List<String>> block = listMono.block();
        return block.stream().flatMap(Collection::stream).collect(Collectors.toList());
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
