package com.clickbus.service.service;

import com.clickbus.service.service.dto.ClientApplicationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ClientApplication.
 */
public interface ClientApplicationService {

    /**
     * Save a clientApplication.
     *
     * @param clientApplicationDTO the entity to save
     * @return the persisted entity
     */
    ClientApplicationDTO save(ClientApplicationDTO clientApplicationDTO);

    /**
     * Get all the clientApplications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClientApplicationDTO> findAll(Pageable pageable);
    
    /**
     * Get all the clientApplications by PlaceID.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClientApplicationDTO> findAllByPlace(Long placeId, Pageable pageable);


    /**
     * Get the "id" clientApplication.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ClientApplicationDTO> findOne(Long id);

    /**
     * Delete the "id" clientApplication.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the clientApplication corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClientApplicationDTO> search(String query, Pageable pageable);
}
