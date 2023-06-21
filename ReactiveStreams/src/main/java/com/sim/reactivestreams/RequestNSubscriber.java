package com.sim.reactivestreams;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Flow;

@Slf4j
public class RequestNSubscriber<T> implements Flow.Subscriber<T>{

    private final Integer N;
    private Flow.Subscription subscription;
    private int count = 0;

    public RequestNSubscriber(Integer n) {
        N = n;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        log.info("item : {}", item);

        if(count++% N == 0){
            log.info("send request");
            this.subscription.request(N);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("error : {}", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("completed");
    }
}
