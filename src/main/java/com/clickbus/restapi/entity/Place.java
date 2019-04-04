package com.clickbus.restapi.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "city_id")
    @ManyToOne
    private City city;
    private String name;
    private String terminalName;
    private String slug;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
