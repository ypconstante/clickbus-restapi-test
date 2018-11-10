package com.clickbus.restapi.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.repository.ClientApplicationRepository;
import com.clickbus.restapi.test.service.ClientApplicationService;

@Service
public class ClientApplicationServiceImpl implements ClientApplicationService {

	@Autowired
	private ClientApplicationRepository repository;

	@Override
	public ClientApplication save(ClientApplication c) {
		return repository.save(c);
	}

}
