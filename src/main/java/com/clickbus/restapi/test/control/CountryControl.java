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

import com.clickbus.restapi.test.dto.CountryDTO;
import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.CountryService;

@RestController()
@RequestMapping(value = "api/country")
public class CountryControl {

	@Autowired
	private CountryService service;

	private static final Logger log = LoggerFactory.getLogger(CountryControl.class);

	@PostMapping()
	public ResponseEntity<Response<CountryDTO>> save(@Valid @RequestBody CountryDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<CountryDTO> response = new Response<CountryDTO>();

		if (result.hasErrors()) {
			log.error("Country does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		Country c = this.service.save(this.convertDtoToEntity(dto));
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private Country convertDtoToEntity(CountryDTO dto) {
		Country c = new Country();
		c.setId(dto.getId());
		c.setName(dto.getName());
		return c;
	}

	private CountryDTO convertEntityToDto(Country c) {
		CountryDTO dto = new CountryDTO();
		dto.setId(c.getId());
		dto.setName(c.getName());
		return dto;
	}
}
