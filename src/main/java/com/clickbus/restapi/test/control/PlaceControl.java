package com.clickbus.restapi.test.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clickbus.restapi.test.dto.PlaceDTO;
import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.CityService;
import com.clickbus.restapi.test.service.PlaceService;


@RestController()
@RequestMapping(value = "api/place")
public class PlaceControl {
	
	@Autowired
	private PlaceService service;
	@Autowired
	private CityService cityService;
	
	private static final Logger log = LoggerFactory.getLogger(PlaceControl.class);
	
	@GetMapping()
	public ResponseEntity<Response<List<PlaceDTO>>> get(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "slug", required = false) String slug) {
		
		log.info(id+" "+slug);
		
		Response<List<PlaceDTO>> place = new Response<List<PlaceDTO>>();
		List<Place> list = new ArrayList<>();
		List<PlaceDTO> listDto = new ArrayList<>();
		
		if (id != null && id >  0) {
			Optional<Place> p = this.service.findById(id);
			if (p.isPresent()) {
				list.add(p.get());
			}
		} else if (slug != null && !slug.isEmpty()){
			list = this.service.findBySlugLikeIgnoreCase(slug);	
		}
		
		list.forEach(f -> listDto.add(this.convertEntityToDto(f)));
		place.setData(listDto);
		return ResponseEntity.ok(place);
	}
	
	@PostMapping()
	public ResponseEntity<Response<PlaceDTO>> save(@Valid @RequestBody PlaceDTO dto, BindingResult result) {

		log.info("Entity to save {}", dto.toString());

		Response<PlaceDTO> response = new Response<PlaceDTO>();
		Place place = this.convertDtoToEntity(dto, result);
		this.verifySlugAlreadyExist(dto.getSlug(), result);
		
		if (result.hasErrors()) {
			log.error("Place does not valid");
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		Place c = this.service.save(place);
		response.setData(this.convertEntityToDto(c));
		return ResponseEntity.ok(response);
	}

	private Place convertDtoToEntity(PlaceDTO dto, BindingResult result) {
		Optional<City> city = cityService.findById(dto.getCityId());
		if (!city.isPresent()) {
			result.addError(new ObjectError("City", "City "+dto.getCityId()+" does not exist"));
		}
		
		Place place = new Place();
		place.setAdress(dto.getAdress());
		place.setName(dto.getName());
		place.setSlug(dto.getSlug());
		place.setTerminalName(dto.getTerminalName());
		place.setCityId(new City());
		place.getCityId().setId(dto.getCityId());
		
		return place;
	}

	private PlaceDTO convertEntityToDto(Place p) {
		PlaceDTO dto = new PlaceDTO();
		dto.setId(p.getId());
		dto.setAdress(p.getAdress());
		dto.setName(p.getName());
		dto.setSlug(p.getSlug());
		dto.setTerminalName(p.getTerminalName());
		dto.setCityId(p.getCityId().getId());
		
		return dto;
	}
	
	private void verifySlugAlreadyExist(String slug, BindingResult result) {
		Optional<Place> place = service.findBySlugIgnoreCase(slug);
		if (place.isPresent()) {
			result.addError(new ObjectError("Place", "Slug "+place.get().getSlug()+" already exist"));
		}
	}
}
