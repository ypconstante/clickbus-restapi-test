package com.clickbus.restapi.test.control;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clickbus.restapi.test.dto.CityDTO;
import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.CityService;

@RestController()
@RequestMapping(value = "api/city")
public class CityControl {

	@Autowired
	private CityService service;

	private static final Logger log = LoggerFactory.getLogger(CityControl.class);

	@PostMapping()
	public ResponseEntity<Response<CityDTO>> save(@Valid @RequestBody CityDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<CityDTO> response = new Response<CityDTO>();

		if (result.hasErrors()) {
			log.error("City does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		City c = this.service.save(this.convertDtoToEntity(dto));
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private City convertDtoToEntity(CityDTO dto) {
		City c = new City();
		c.setId(dto.getId());
		c.setName(dto.getName());
		c.setStateId(dto.getStateId());

		return c;
	}

	private CityDTO convertEntityToDto(City c) {
		CityDTO dto = new CityDTO();
		dto.setId(c.getId());
		dto.setName(c.getName());
		dto.setStateId(c.getStateId());

		return dto;
	}
}
