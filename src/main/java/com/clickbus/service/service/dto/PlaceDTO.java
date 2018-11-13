package com.clickbus.service.service.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the Place entity.
 */
@Data 
@EqualsAndHashCode(callSuper = true, exclude = "clientApplications")
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "terminalName", "address", "slug", "cityId", "clientApplications"})
public class PlaceDTO extends AbstractAuditingDto {

    private static final long serialVersionUID = 2346312361734613231L;

    private Long id;

    @NotNull(message = "the name can't be null")
    @Size(min = 2, max = 255)
    private String name;

    @NotNull(message = "the terminalName can't be null")
    @Size(min = 2, max = 255)
    private String terminalName;

    @NotNull(message = "the address can't be null")
    private String address;

    // @NotNull Caso não seja informado, um slug será produzido a partir do nome
    @Size(min = 2, max = 255)
    private String slug;

    private Set<ClientApplicationDTO> clientApplications = new HashSet<>();

    private Long cityId;

}
