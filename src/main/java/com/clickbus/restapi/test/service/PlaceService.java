package com.clickbus.restapi.test.service;

import java.util.Optional;

import com.clickbus.restapi.test.entity.Place;


public interface PlaceService {
	
	Optional<Place> findById(Long id);
	
	Optional<Place> findBySlugLikeIgnoraCase(String slug);
}
