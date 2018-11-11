package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CountryDTO {
	
	private Long id;
	@Length(max = 100, message = "The name max size is 100")
	@Length(min = 3, message = "The name min size is 3")
	@NotNull(message = "Name cannot be null")
	private String name;
}
