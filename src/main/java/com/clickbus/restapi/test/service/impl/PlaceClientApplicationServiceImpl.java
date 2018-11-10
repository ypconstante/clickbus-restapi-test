package com.clickbus.restapi.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.PlaceClientApplication;
import com.clickbus.restapi.test.repository.PlaceClientApplicationRepository;
import com.clickbus.restapi.test.service.PlaceClientApplicationService;

@Service
public class PlaceClientApplicationServiceImpl implements PlaceClientApplicationService {

	@Autowired
	private PlaceClientApplicationRepository repository;

	@Override
	public PlaceClientApplication save(PlaceClientApplication c) {
		return repository.save(c);
	}

}
