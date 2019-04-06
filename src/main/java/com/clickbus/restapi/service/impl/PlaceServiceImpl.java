package com.clickbus.restapi.service.impl;

import java.util.Collection;

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
}
