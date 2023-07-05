package com.sim.sse.controller;

import lombok.Data;

@Data
public class Event {
    private String type;
    private String message;
}
