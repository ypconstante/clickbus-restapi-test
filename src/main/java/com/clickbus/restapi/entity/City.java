package com.clickbus.restapi.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "state_id")
    @ManyToOne
    private State state;
    @Column
    private String name;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
