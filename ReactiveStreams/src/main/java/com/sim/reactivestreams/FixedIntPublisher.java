package com.sim.reactivestreams;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow;

public class FixedIntPublisher implements Flow.Publisher<FixedIntPublisher.Result>{

    @Override
    public void subscribe(Flow.Subscriber<? super Result> subscriber) {
        var numbers = Collections.synchronizedList(
                new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7))
        );

        Iterator<Integer> iterator = numbers.iterator();
        var subscription = new IntSubscription(subscriber, iterator);
        subscriber.onSubscribe(subscription);
    }

    @Data
    public static class Result {
        private final Integer value;
        private final Integer requestCount;
    }


}
