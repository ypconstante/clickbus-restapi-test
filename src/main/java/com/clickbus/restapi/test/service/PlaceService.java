package com.clickbus.restapi.test.service;

import java.util.List;
import java.util.Optional;

import com.clickbus.restapi.test.entity.Place;


public interface PlaceService {
	
	Optional<Place> findById(Long id);
	
	List<Place> findBySlugLikeIgnoreCase(String slug);
	
	Optional<Place> findBySlugIgnoreCase(String slug);
	
	Place save(Place p);
}
