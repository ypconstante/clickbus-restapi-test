package com.clickbus.restapi.repository;

import java.util.List;

import com.clickbus.restapi.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findAllBySlugContaining(String slug);
}
