package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ClientApplicationDTO {
	private Long id;
	@Length(min = 3, max = 100, message = "The name min size is 3 and max size is 100")
	@NotNull(message = "Name cannot be null")
	private String name;
	@Length(min = 3, max = 255, message = "Public name min size is 3 and max size is 255")
	@NotNull(message = "Public name cannot be null")
	private String publicName;

}
