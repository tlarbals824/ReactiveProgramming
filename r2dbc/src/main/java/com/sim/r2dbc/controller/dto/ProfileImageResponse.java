package com.sim.r2dbc.controller.dto;

import lombok.Data;

@Data
public class ProfileImageResponse {
    private final String id;
    private final String name;
    private final String url;
}
