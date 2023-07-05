package com.sim.websocket.handler;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {

    // map은 왜 안될까?
    private static Map<String, Sinks.Many<Chat>> chatSinkMap = new ConcurrentHashMap<>();

    public Flux<Chat> register(String iam){
        Sinks.Many<Chat> sink = Sinks.many().unicast().onBackpressureBuffer();
        chatSinkMap.put(iam, sink);

        return sink.asFlux();
    }

    public boolean sendChat(String iam, Chat chat){
        Sinks.Many<Chat> sink = chatSinkMap.get(iam);
        if(Objects.isNull(sink)) return false;

        sink.tryEmitNext(chat);

        return true;
    }
}
