package com.example.handler;

public class TaskRunner {
    public Thread schedule(Runnable job) {
        Thread thread = new Thread(job);
        thread.start();
        return thread;
    }
}
