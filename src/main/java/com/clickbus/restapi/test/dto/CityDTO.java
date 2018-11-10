package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import com.clickbus.restapi.test.entity.State;

import lombok.Data;

@Data
public class CityDTO {

	private Long id;
	@NotNull(message = "State cannot be null")
	private State stateId;
	@Length(max = 100, message = "The name max size is 100")
	private String name;
}
