package com.clickbus.restapi.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Country {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
