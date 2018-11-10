package com.clickbus.restapi.test.service;

import java.util.Optional;

import com.clickbus.restapi.test.entity.State;


public interface StateService {
	
	State save(State c); 
	
	Optional<State> findById(Long id);
}
