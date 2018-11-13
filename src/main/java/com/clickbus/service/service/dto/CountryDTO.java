package com.clickbus.service.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the Country entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CountryDTO extends AbstractAuditingDto {

    private static final long serialVersionUID = -7660637602753672145L;

    private Long id;

    @NotNull(message = "the name can't be null")
    @Size(min = 2, max = 100)
    private String name;

	public CountryDTO(Long id) {
		super();
		this.id = id;
	}
    
}
