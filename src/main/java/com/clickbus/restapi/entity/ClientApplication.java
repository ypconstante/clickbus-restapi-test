package com.clickbus.restapi.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data()
@Entity
@ToString(exclude = "places")
@EqualsAndHashCode(exclude = "places")
public class ClientApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String publicName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToMany
    @JoinTable(
        name = "place_client_application",
        joinColumns = @JoinColumn(name = "client_id"),
        inverseJoinColumns = @JoinColumn(name = "place_id"))
    private Collection<Place> places = Collections.emptyList();
}
