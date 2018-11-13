package com.clickbus.service.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the ClientApplication entity.
 */
@Data 
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "publicName","createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class ClientApplicationDTO extends AbstractAuditingDto {

    private static final long serialVersionUID = -2196473443547343174L;

    private Long id;

    @NotNull(message = "the name can't be null")
    @Size(min = 2, max = 100)
    private String name;

    @NotNull(message = "the publicNama can't be null")
    private String publicName;
    
}
