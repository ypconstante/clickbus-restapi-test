package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;


@Data
public class PlaceDTO {
	private Long id;
	@Length(min = 3, max = 255, message = "The name min size is 3 and max size is 255")
	@NotNull(message = "Name cannot be null")
	private String name;
	@Length(min = 3, max = 255, message = "The name min size is 3 and max size is 255")
	@NotNull(message = "Terminal name cannot be null")
	private String terminalName;
	@NotNull(message = "Slug cannot be null")
	private String slug;
	@NotNull(message = "Adress cannot be null")
	private String adress;
	@NotNull(message = "City cannot be null")
	private Long cityId;
	
}
