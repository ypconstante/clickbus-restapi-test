package com.clickbus.restapi.test.control;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clickbus.restapi.test.dto.StateDTO;
import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.entity.State;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.CountryService;
import com.clickbus.restapi.test.service.StateService;

@RestController()
@RequestMapping(value = "api/state")
public class StateControl {

	@Autowired
	private StateService service;
	@Autowired
	private CountryService countryService;
	
	private static final Logger log = LoggerFactory.getLogger(StateControl.class);

	@PostMapping()
	public ResponseEntity<Response<StateDTO>> save(@Valid @RequestBody StateDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<StateDTO> response = new Response<StateDTO>();
		
		State state = this.convertDtoToEntity(dto, result);
		
		if (result.hasErrors()) {
			log.error("State does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		State c = this.service.save(state);
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private State convertDtoToEntity(StateDTO dto, BindingResult result) {
		Optional<Country> country = this.countryService.findById(dto.getCountryId());
		if (!country.isPresent()) {
			result.addError(new ObjectError("Country", "Country "+dto.getCountryId()+" does not exist"));
		}
		
		State c = new State();
		c.setId(dto.getId());
		c.setName(dto.getName());
		c.setCountryId(new Country());
		c.getCountryId().setId(dto.getCountryId());
		return c;
	}

	private StateDTO convertEntityToDto(State c) {
		StateDTO dto = new StateDTO();
		dto.setId(c.getId());
		dto.setName(c.getName());
		dto.setCountryId(c.getCountryId().getId());

		return dto;
	}
}
