package com.clickbus.restapi.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class State {
    private Long id;
    private Country country;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
