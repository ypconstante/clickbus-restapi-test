package com.clickbus.service.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Task entity.
 * @author The JHipster team.
 */
@ApiModel(description = "Task entity. @author The JHipster team.")
@Entity
@Table(name = "place", uniqueConstraints = @UniqueConstraint(columnNames = {"slug"}, name = "slug_uidx"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "place")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"clientApplications", "city"})
@ToString(exclude = {"clientApplications", "city"})
public class Place extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
      
    //@Field()
    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "slug", length = 255, nullable = false, unique = true)
    private String slug;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "terminal_name", length = 255, nullable = false)
    private String terminalName;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "place_client_application",
               joinColumns = @JoinColumn(name = "places_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "client_applications_id", referencedColumnName = "id"))
    private Set<ClientApplication> clientApplications = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("places")
    private City city;

}
