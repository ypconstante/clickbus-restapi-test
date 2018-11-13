package com.clickbus.service.service.impl;

import com.clickbus.service.service.CountryService;
import com.clickbus.service.domain.Country;
import com.clickbus.service.repository.CountryRepository;
import com.clickbus.service.repository.search.CountrySearchRepository;
import com.clickbus.service.service.dto.CountryDTO;
import com.clickbus.service.service.mapper.CountryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Country.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private CountryRepository countryRepository;

    private CountryMapper countryMapper;

    private CountrySearchRepository countrySearchRepository;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper, CountrySearchRepository countrySearchRepository) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.countrySearchRepository = countrySearchRepository;
    }

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CountryDTO save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);

        Country country = countryMapper.toEntity(countryDTO);
        country = countryRepository.save(country);
        CountryDTO result = countryMapper.toDto(country);
        countrySearchRepository.save(country);
        return result;
    }

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countryRepository.findAll(pageable)
            .map(countryMapper::toDto);
    }


    /**
     * Get one country by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CountryDTO> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id)
            .map(countryMapper::toDto);
    }

    /**
     * Delete the country by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
        countrySearchRepository.deleteById(id);
    }

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Countries for query {}", query);
        return countrySearchRepository.search(queryStringQuery(query), pageable)
            .map(countryMapper::toDto);
    }
}
