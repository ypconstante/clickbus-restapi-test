package com.clickbus.restapi.test.service;

import java.util.Optional;

import com.clickbus.restapi.test.entity.ClientApplication;


public interface ClientApplicationService {
	
	ClientApplication save(ClientApplication c);
	
	Optional<ClientApplication> findById(Long id);
}
