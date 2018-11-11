package com.clickbus.restapi.test.service;

import java.util.Optional;

import com.clickbus.restapi.test.entity.City;


public interface CityService {
	
	City save(City c);
	
	Optional<City> findById(Long id);
}
