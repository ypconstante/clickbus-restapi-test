package com.clickbus.restapi.test.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.response.Response;
import com.clickbus.restapi.test.service.PlaceService;


@RestController()
@RequestMapping(value = "api/place")
public class PlaceControl {
	
	@Autowired
	private PlaceService service;
	
	private static final Logger log = LoggerFactory.getLogger(PlaceControl.class);
	
	@GetMapping()
	public ResponseEntity<Response<List<Place>>> get(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "slug", required = false) String slug) {
		
		log.info(id+" "+slug);
		
		Response<List<Place>> place = new Response<List<Place>>();
		List<Place> list = new ArrayList<>();
		if (id != null && id >  0) {
			Optional<Place> p = this.service.findById(id);
			if (p.isPresent()) {
				list.add(p.get());
			}
		} else if (slug != null && !slug.isEmpty()){
			Optional<Place> p = this.service.findBySlugLikeIgnoraCase(slug);
			if (p.isPresent()) {
				list.add(p.get());
			}
		}
		
		place.setData(list);
		return ResponseEntity.ok(place);
	}

}
