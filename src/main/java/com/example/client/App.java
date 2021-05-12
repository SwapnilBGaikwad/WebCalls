package com.example.client;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws InterruptedException {
        GreetingWebClient gwc = new GreetingWebClient();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletionService<String> completion = new ExecutorCompletionService<>(executor);
        for (int i = 0; i < 1000; i++) {
            completion.submit(() -> {
                List<String> result = gwc.getResult();
                System.out.println("Response :" + result);
            }, "");
        }
        for (int i = 0; i < 1000; ++i) {
            completion.take();
        }
        executor.shutdown();
    }
}
