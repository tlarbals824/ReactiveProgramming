package com.sim.sse.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class NotificationService {
    private static Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<String> getMessageFromSink(){
        return sink.asFlux();
    }

    public void tryEmitNext(String message){
        sink.tryEmitNext(message);
    }

}
