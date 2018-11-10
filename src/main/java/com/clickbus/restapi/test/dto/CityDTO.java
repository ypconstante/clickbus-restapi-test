package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CityDTO {

	private Long id;
	@NotNull(message = "State cannot be null")
	private Long stateId;
	@Length(min = 3, max = 100, message = "The name min size is 3 and max size is 100")
	@NotNull(message = "Name cannot be null")
	private String name;
}
