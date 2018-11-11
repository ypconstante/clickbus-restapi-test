package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PlaceClientApplicationDTO {
	private Long id;
	@NotNull(message = "Place cannot be null")
	private Long placeId;
	@NotNull(message = "Client cannot be null")
	private Long clientId;
}
