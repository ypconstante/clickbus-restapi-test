package com.clickbus.restapi.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.clickbus.restapi.test.entity.PlaceClientApplication;

@Transactional(readOnly = true)
public interface PlaceClientApplicationRepository extends JpaRepository<PlaceClientApplication, Long>{
	
	List<PlaceClientApplication> findByPlaceIdIdOrderByClientIdId(Long id);
}