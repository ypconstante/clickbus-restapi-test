package com.clickbus.restapi.test.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.clickbus.restapi.test.entity.Place;

@Transactional(readOnly = true)
public interface PlaceRepository extends JpaRepository<Place, Long>{
	
	List<Place> findBySlugLikeIgnoreCase(String slug);
	
	Optional<Place> findBySlugIgnoreCase(String slug);
	
	@Query(value = "select p.id, p.name, p.slug, c.name, s.name, ca.name from Place p "
			+ "join p.cityId c "
			+ "join c.stateId s "
			+ "join s.countryId ca "
			+ "order by p.name")
	List<Object[]> findAllPlaces();
}
