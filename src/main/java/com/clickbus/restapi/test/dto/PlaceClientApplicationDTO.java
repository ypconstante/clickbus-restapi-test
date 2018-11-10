package com.clickbus.restapi.test.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PlaceClientApplicationDTO {
	private Long id;
	@NotNull
	private Long placeId;
	@NotNull
	private Long clientId;
}
