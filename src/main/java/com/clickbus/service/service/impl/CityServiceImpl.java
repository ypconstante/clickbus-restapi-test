package com.clickbus.service.service.impl;

import com.clickbus.service.service.CityService;
import com.clickbus.service.domain.City;
import com.clickbus.service.repository.CityRepository;
import com.clickbus.service.repository.StateRepository;
import com.clickbus.service.repository.search.CitySearchRepository;
import com.clickbus.service.service.dto.CityDTO;
import com.clickbus.service.service.mapper.CityMapper;
import com.clickbus.service.web.rest.errors.InvalidDataException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing City.
 */
@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private CityRepository cityRepository;

    private StateRepository stateRepository;
    
    private CityMapper cityMapper;

    private CitySearchRepository citySearchRepository;

    public CityServiceImpl(CityRepository cityRepository,
                           StateRepository stateRepository,
                           CityMapper cityMapper,
                           CitySearchRepository citySearchRepository) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.cityMapper = cityMapper;
        this.citySearchRepository = citySearchRepository;
    }

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CityDTO save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);

        checkState(cityDTO);

        City city = cityMapper.toEntity(cityDTO);
        
        city = cityRepository.save(city);
        CityDTO result = cityMapper.toDto(city);
        citySearchRepository.save(city);
        return result;
    }

    private void checkState(CityDTO cityDTO) {
        this.stateRepository.findById(cityDTO.getStateId())
            .orElseThrow(() -> new InvalidDataException("The State "+cityDTO.getStateId()+" is invalid", "CITY"));
    }

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return cityRepository.findAll(pageable)
            .map(cityMapper::toDto);
    }


    /**
     * Get one city by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CityDTO> findOne(Long id) {
        log.debug("Request to get City : {}", id);
        return cityRepository.findById(id)
            .map(cityMapper::toDto);
    }

    /**
     * Delete the city by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete City : {}", id);
        cityRepository.deleteById(id);
        citySearchRepository.deleteById(id);
    }

    /**
     * Search for the city corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cities for query {}", query);
        return citySearchRepository.search(queryStringQuery(query), pageable)
            .map(cityMapper::toDto);
    }
}
