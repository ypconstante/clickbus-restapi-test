package com.clickbus.restapi.service.api;

import java.util.Collection;

import com.clickbus.restapi.entity.Place;

public interface PlaceService {
    Collection<Place> findAll();

    Collection<Place> findAllBySlugContaining(String slug);
}
