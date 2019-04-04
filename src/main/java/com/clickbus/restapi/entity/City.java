package com.clickbus.restapi.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class City {
    private Long id;
    private State state;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
