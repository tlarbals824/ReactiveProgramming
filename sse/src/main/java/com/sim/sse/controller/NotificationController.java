package com.sim.sse.controller;

import com.sim.sse.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService = new NotificationService();
    private static AtomicInteger lastEventId = new AtomicInteger(1);

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getNotification() {
        return notificationService.getMessageFromSink()
                .map(message -> {
                    String id = lastEventId.getAndIncrement() + "";
                    return ServerSentEvent.builder(message)
                            .event("notification")
                            .id(id)
                            .build();
                });
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> addNotification(@RequestBody Event event) {
        final String message = event.getType() + ": " + event.getMessage();
        notificationService.tryEmitNext(message);
        return Mono.just("ok");
    }

}
