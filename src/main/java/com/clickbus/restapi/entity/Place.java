package com.clickbus.restapi.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Place {
    private Long id;
    private City city;
    private String name;
    private String terminalName;
    private String slug;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
