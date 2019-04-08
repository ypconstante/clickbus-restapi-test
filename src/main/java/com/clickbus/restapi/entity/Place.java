package com.clickbus.restapi.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "clientApplications")
@EqualsAndHashCode(exclude = "clientApplications")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "city_id")
    @ManyToOne
    private City city;
    @Column
    private String name;
    @Column
    private String terminalName;
    @Column
    private String slug;
    @Column
    private String address;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "places")
    private Collection<ClientApplication> clientApplications = Collections.emptyList();
}
