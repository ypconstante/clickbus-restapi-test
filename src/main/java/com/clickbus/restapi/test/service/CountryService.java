package com.clickbus.restapi.test.service;

import java.util.Optional;

import com.clickbus.restapi.test.entity.Country;


public interface CountryService {
	
	Country save(Country c);
	
	Optional<Country> findById(Long id);
}
