package com.example.handler;

import com.example.client.GreetingWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        TaskRunner taskRunner = new TaskRunner();
        GreetingWebClient gwc = new GreetingWebClient();
        for (int i = 0; i < 100; i++) {
            taskRunner.schedule(() -> System.out.println(gwc.getResult()));
        }
    }
}
