package com.clickbus.restapi.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.repository.CityRepository;
import com.clickbus.restapi.test.service.CityService;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private CityRepository repository;

	@Override
	public City save(City c) {
		return repository.save(c);
	}

}
