package com.clickbus.restapi.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.clickbus.restapi.test.entity.City;

@Transactional(readOnly = true)
public interface CityRepository extends JpaRepository<City, Long>{
}