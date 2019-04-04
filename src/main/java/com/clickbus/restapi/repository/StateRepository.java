package com.clickbus.restapi.repository;

import com.clickbus.restapi.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
}
