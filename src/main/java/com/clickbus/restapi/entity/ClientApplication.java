package com.clickbus.restapi.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClientApplication {
    private Long id;
    private String name;
    private String publicName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
