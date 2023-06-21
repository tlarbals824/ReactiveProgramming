package com.sim.reactivestreamshotpublisher;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveStreamsHotPublisherApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(ReactiveStreamsHotPublisherApplication.class, args);

//        simpleColdPublisher();
        simpleHotPublisher();
    }

    @SneakyThrows
    private static void simpleColdPublisher() {
        var publisher = new SimpleColdPublisher();

        var subscriber = new SimpleNamedSubscriber<>("subscriber1");
        publisher.subscribe(subscriber);

        Thread.sleep(5000);

        var subscriber2 = new SimpleNamedSubscriber<>("subscriber2");
        publisher.subscribe(subscriber2);
    }

    @SneakyThrows
    private static void simpleHotPublisher(){
        // prepare publisher
        var publisher = new SimpleHotPublisher();

        // prepare subscriber1
        var subscriber = new SimpleNamedSubscriber<>("subscriber1");
        publisher.subscribe(subscriber);

        // cancel after 5s
        Thread.sleep(5000);
        subscriber.cancel();

        // prepare subscriber2,3
        var subscriber2 = new SimpleNamedSubscriber<>("subscriber2");
        var subscriber3 = new SimpleNamedSubscriber<>("subscriber3");
        publisher.subscribe(subscriber2);
        publisher.subscribe(subscriber3);

        // cancel after 5s
        Thread.sleep(5000);
        subscriber2.cancel();
        subscriber3.cancel();


        Thread.sleep(1000);

        var subscriber4 = new SimpleNamedSubscriber<>("subscriber4");
        publisher.subscribe(subscriber4);

        // cancel after 5s
        Thread.sleep(5000);
        subscriber4.cancel();

        // shutdown publisher
        publisher.shutdown();

    }

}
