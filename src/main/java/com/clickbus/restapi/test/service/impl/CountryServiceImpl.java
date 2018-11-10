package com.clickbus.restapi.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.repository.CountryRepository;
import com.clickbus.restapi.test.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository repository;

	@Override
	public Country save(Country c) {
		return repository.save(c);
	}

}
