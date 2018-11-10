package com.clickbus.restapi.test.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CountryDTO {
	
	private Long id;
	@Length(max = 100, message = "The name max size is 100")
	private String name;
}
