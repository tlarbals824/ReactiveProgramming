package com.sim.reactivestreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Flow;

@SpringBootApplication
public class ReactiveStreamsApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ReactiveStreamsApplication.class, args);

        Flow.Publisher publisher = new FixedIntPublisher();
        Flow.Subscriber subscriber = new RequestNSubscriber<>(3);
        publisher.subscribe(subscriber);

        Thread.sleep(100);
    }

}
