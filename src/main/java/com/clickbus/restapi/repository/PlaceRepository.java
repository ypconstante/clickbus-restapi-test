package com.clickbus.restapi.repository;

import com.clickbus.restapi.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
