package com.sim.reactorpattern;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args){
        List<EventLoop> eventLoops = List.of(new EventLoop(8080), new EventLoop(8081));
        eventLoops.forEach(EventLoop::run);
    }
}
