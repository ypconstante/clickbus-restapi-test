package com.clickbus.restapi.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.clickbus.restapi.test.entity.State;

@Transactional(readOnly = true)
public interface StateRepository extends JpaRepository<State, Long>{
}
