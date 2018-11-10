package com.clickbus.restapi.test.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.restapi.test.entity.State;
import com.clickbus.restapi.test.repository.StateRepository;
import com.clickbus.restapi.test.service.StateService;

@Service
public class StateServiceImpl implements StateService {

	@Autowired
	private StateRepository repository;

	@Override
	public State save(State c) {
		return repository.save(c);
	}

	@Override
	public Optional<State> findById(Long id) {
		return repository.findById(id);
	}

}
