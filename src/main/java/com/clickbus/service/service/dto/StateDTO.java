package com.clickbus.service.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the State entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StateDTO extends AbstractAuditingDto {

    private static final long serialVersionUID = -8092521575764317559L;

    private Long id;

    @NotNull(message = "the name can't be null")
    @Size(min = 2, max = 255)
    private String name;

    private Long countryId;

	public StateDTO(Long id) {
		super();
		this.id = id;
		this.name = null;
	}
    
}
