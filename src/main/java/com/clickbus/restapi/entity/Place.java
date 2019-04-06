package com.clickbus.restapi.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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
    private String name;
    private String terminalName;
    private String slug;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "places")
    private Collection<ClientApplication> clientApplications;

    public Collection<ClientApplication> getClientApplications() {
        if (this.clientApplications == null) {
            this.clientApplications = Collections.emptyList();
        }
        return this.clientApplications;
    }
}
