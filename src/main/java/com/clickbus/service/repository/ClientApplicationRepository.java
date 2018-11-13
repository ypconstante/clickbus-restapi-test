package com.clickbus.service.repository;

import com.clickbus.service.domain.ClientApplication;
import com.clickbus.service.domain.Place;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ClientApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientApplicationRepository extends JpaRepository<ClientApplication, Long> {

	
	@Query(value = "select distinct client from ClientApplication client left join client.places place where place.id = :id ", 
			countQuery = "select count(distinct client) from ClientApplication client left join client.places place where place.id = :id ")
	Page<ClientApplication> findAllByPlaceId(@Param("id") Long id, Pageable pageable);
}
