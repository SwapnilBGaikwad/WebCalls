package com.example.handler;

public class TaskRunner {
    public Thread schedule(int jobId, Runnable job) {
        Thread thread = new Thread(job, "Job Id " + jobId);
        thread.start();
        return thread;
    }
}
