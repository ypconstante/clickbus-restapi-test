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

import com.clickbus.restapi.test.dto.PlaceClientApplicationDTO;
import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.entity.PlaceClientApplication;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.ClientApplicationService;
import com.clickbus.restapi.test.service.PlaceClientApplicationService;
import com.clickbus.restapi.test.service.PlaceService;

@RestController()
@RequestMapping(value = "api/place-client-application")
public class PlaceClientApplicationControl {

	@Autowired
	private PlaceClientApplicationService service;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private ClientApplicationService clientAppService;
	
	private static final Logger log = LoggerFactory.getLogger(PlaceClientApplicationControl.class);

	@PostMapping()
	public ResponseEntity<Response<PlaceClientApplicationDTO>> save(@Valid @RequestBody PlaceClientApplicationDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<PlaceClientApplicationDTO> response = new Response<PlaceClientApplicationDTO>();
		
		PlaceClientApplication placeClientApplication = this.convertDtoToEntity(dto, result);
		
		if (result.hasErrors()) {
			log.error("PlaceClientApplication does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		PlaceClientApplication c = this.service.save(placeClientApplication);
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private PlaceClientApplication convertDtoToEntity(PlaceClientApplicationDTO dto, BindingResult result) {
		Optional<Place> place = this.placeService.findById(dto.getPlaceId());
		if (!place.isPresent()) {
			result.addError(new ObjectError("Place", "Place "+dto.getPlaceId()+" does not exist"));
		}
		
		Optional<ClientApplication> clientApp = this.clientAppService.findById(dto.getClientId());
		if (!clientApp.isPresent()) {
			result.addError(new ObjectError("ClientApplication", "Client "+dto.getClientId()+" does not exist"));
		}
		
		PlaceClientApplication c = new PlaceClientApplication();
		c.setId(dto.getId());
		c.setPlaceId(new Place());
		c.getPlaceId().setId(dto.getPlaceId());
		c.setClientId(new ClientApplication());
		c.getClientId().setId(dto.getClientId());
		return c;
	}

	private PlaceClientApplicationDTO convertEntityToDto(PlaceClientApplication c) {
		PlaceClientApplicationDTO dto = new PlaceClientApplicationDTO();
		dto.setId(c.getId());
		dto.setClientId(c.getId());
		dto.setPlaceId(c.getPlaceId().getId());

		return dto;
	}
}
