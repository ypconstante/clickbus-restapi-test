package com.clickbus.service.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the City entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CityDTO extends AbstractAuditingDto {

    private static final long serialVersionUID = -6760804659385009864L;

    private Long id;

    @NotNull(message = "the name can't be null")
    @Size(min = 2, max = 100)
    private String name;

    @NotNull(message = "the stateId can't be null")
    private Long stateId;
    
    private String stateName;

	public CityDTO(Long id) {
		super();
		this.id = id;
	}
    
}
