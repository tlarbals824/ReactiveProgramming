package com.sim.redisnotification.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateResponse {
    private String id;
    private String name;
    private String url;
}
