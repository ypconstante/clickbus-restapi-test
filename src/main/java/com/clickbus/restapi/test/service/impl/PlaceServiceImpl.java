package com.clickbus.restapi.test.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.repository.PlaceRepository;
import com.clickbus.restapi.test.service.PlaceService;

@Service
public class PlaceServiceImpl implements PlaceService{

	@Autowired
	private PlaceRepository repository;
	
	@Override
	public Optional<Place> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Optional<Place> findBySlugLikeIgnoraCase(String slug) {
		return repository.findBySlugLikeIgnoreCase(slug);
	}

}
