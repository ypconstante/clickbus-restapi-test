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

import com.clickbus.restapi.test.dto.ClientApplicationDTO;
import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.ClientApplicationService;

@RestController()
@RequestMapping(value = "api/client-application")
public class ClientApplicationControl {

	@Autowired
	private ClientApplicationService service;

	private static final Logger log = LoggerFactory.getLogger(ClientApplicationControl.class);

	@PostMapping()
	public ResponseEntity<Response<ClientApplicationDTO>> save(@Valid @RequestBody ClientApplicationDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<ClientApplicationDTO> response = new Response<ClientApplicationDTO>();

		if (result.hasErrors()) {
			log.error("ClientApplication does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		ClientApplication c = this.service.save(this.convertDtoToEntity(dto));
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private ClientApplication convertDtoToEntity(ClientApplicationDTO dto) {
		ClientApplication c = new ClientApplication();
		c.setId(dto.getId());
		c.setName(dto.getName());
		c.setPublicName(dto.getPublicName());
		return c;
	}

	private ClientApplicationDTO convertEntityToDto(ClientApplication c) {
		ClientApplicationDTO dto = new ClientApplicationDTO();
		dto.setId(c.getId());
		dto.setName(c.getName());
		dto.setPublicName(c.getPublicName());
		return dto;
	}
}
