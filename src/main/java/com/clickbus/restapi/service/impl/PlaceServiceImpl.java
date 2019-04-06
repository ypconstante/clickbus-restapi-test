package com.clickbus.restapi.service.impl;

import java.util.Collection;
import java.util.Optional;

import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.repository.PlaceRepository;
import com.clickbus.restapi.service.api.PlaceService;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public Collection<Place> findAll() {
        return this.placeRepository.findAll();
    }

    @Override
    public Collection<Place> findAllBySlugContaining(String slug) {
        return this.placeRepository.findAllBySlugContaining(slug);
    }

    @Override
    public Optional<Place> findById(Long id) {
        return this.placeRepository.findById(id);
    }
}
