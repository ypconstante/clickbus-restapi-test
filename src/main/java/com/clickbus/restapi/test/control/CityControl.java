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

import com.clickbus.restapi.test.dto.CityDTO;
import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.entity.State;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.CityService;
import com.clickbus.restapi.test.service.StateService;

@RestController()
@RequestMapping(value = "api/city")
public class CityControl {

	@Autowired
	private CityService service;
	@Autowired
	private StateService stateService;

	private static final Logger log = LoggerFactory.getLogger(CityControl.class);

	@PostMapping()
	public ResponseEntity<Response<CityDTO>> save(@Valid @RequestBody CityDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<CityDTO> response = new Response<CityDTO>();
		City city = this.convertDtoToEntity(dto, result);
		
		if (result.hasErrors()) {
			log.error("City does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		City c = this.service.save(city);
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private City convertDtoToEntity(CityDTO dto, BindingResult result) {
		Optional<State> state = this.stateService.findById(dto.getStateId());
		if (!state.isPresent()) {
			result.addError(new ObjectError("Country", "State "+dto.getStateId()+" does not exist"));
		}
		
		City c = new City();
		c.setId(dto.getId());
		c.setName(dto.getName());
		c.setStateId(new State());
		c.getStateId().setId(dto.getStateId());

		return c;
	}

	private CityDTO convertEntityToDto(City c) {
		CityDTO dto = new CityDTO();
		dto.setId(c.getId());
		dto.setName(c.getName());
		dto.setStateId(c.getStateId().getId());

		return dto;
	}
}
