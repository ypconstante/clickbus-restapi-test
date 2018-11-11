package com.clickbus.restapi.test.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.repository.PlaceRepository;
import com.clickbus.restapi.test.service.PlaceService;

@Service
public class PlaceServiceImpl implements PlaceService{

	@Autowired
	private PlaceRepository repository;
	
	@Override
	@Cacheable("placeById")
	public Optional<Place> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Place> findBySlugLikeIgnoreCase(String slug) {
	return repository.findBySlugLikeIgnoreCase("%"+slug+"%");
	}

	@Override
	public Optional<Place> findBySlugIgnoreCase(String slug) {
		return repository.findBySlugIgnoreCase(slug);
	}

	@Override
	@CachePut("placeById")
	public Place save(Place p) {
		return repository.save(p);
	}

	@Override
	public List<Object[]> findAllPlaces() {
		return repository.findAllPlaces();
	}

}
