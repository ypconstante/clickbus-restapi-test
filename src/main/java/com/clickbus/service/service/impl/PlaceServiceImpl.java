package com.clickbus.service.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.clickbus.service.domain.Place;
import com.clickbus.service.repository.CityRepository;
import com.clickbus.service.repository.ClientApplicationRepository;
import com.clickbus.service.repository.PlaceRepository;
import com.clickbus.service.repository.search.PlaceSearchRepository;
import com.clickbus.service.service.PlaceService;
import com.clickbus.service.service.dto.PlaceDTO;
import com.clickbus.service.service.dto.PlaceSimpleDTO;
import com.clickbus.service.service.dto.projections.PlaceDetailsDTO;
import com.clickbus.service.service.mapper.PlaceMapper;
import com.clickbus.service.service.util.SlugUtil;
import com.clickbus.service.web.rest.errors.InvalidDataException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Place.
 */
@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final Logger log = LoggerFactory.getLogger(PlaceServiceImpl.class);

    private PlaceRepository placeRepository;

    private CityRepository cityRepository;
    
    private ClientApplicationRepository clientApplicationRepository;

    private PlaceMapper placeMapper;

    private PlaceSearchRepository placeSearchRepository;

	public PlaceServiceImpl(PlaceRepository placeRepository,
                            CityRepository cityRepository,
                            ClientApplicationRepository clientApplicationRepository,
                            PlaceMapper placeMapper,
                            PlaceSearchRepository placeSearchRepository) {
        this.placeRepository = placeRepository;
        this.cityRepository = cityRepository;
        this.clientApplicationRepository = clientApplicationRepository;
        this.placeMapper = placeMapper;
        this.placeSearchRepository = placeSearchRepository;
    }

    /**
     * Make a rebuild from Place indexes whether the indexes are not created
     */
    @PostConstruct
    public void init() {
        log.debug("Reindexing onInit... ");
        if (this.placeSearchRepository.count() == 0) {
            reindex();
        }
    }

    /**
     * Save a place.
     *
     * @param placeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PlaceDTO save(PlaceDTO placeDTO) {
        log.debug("Request to save Place : {}", placeDTO);
        
        checkSlug(placeDTO);
        checkCity(placeDTO);
        checkClients(placeDTO);
        
        Place place = placeMapper.toEntity(placeDTO);
        
        place = placeRepository.save(place);
        PlaceDTO result = placeMapper.toDto(place);
        placeSearchRepository.save(place);
        return result;
    }

    private void checkSlug(PlaceDTO placeDTO) {
    	
    	if (StringUtils.isBlank(placeDTO.getSlug())) {
    		if (StringUtils.isNotBlank(placeDTO.getName())) {
    			placeDTO.setSlug(SlugUtil.makeSlug(placeDTO.getName()));
    		}
    	}
    	
    	if (this.placeRepository.existsBySlugIgnoreCase(placeDTO.getSlug())) {
    		throw new InvalidDataException("The Slug "+placeDTO.getSlug()+" already exists", "PLACE");   
    	}
    	
    }
    
    private void checkCity(PlaceDTO placeDTO) {
        this.cityRepository.findById(placeDTO.getCityId())
            .orElseThrow(() -> new InvalidDataException("The City "+placeDTO.getCityId()+" is invalid", "PLACE"));          
    }
    
    private void checkClients(PlaceDTO placeDTO) {
    	
    	if(placeDTO.getClientApplications() != null && !placeDTO.getClientApplications().isEmpty()) {
    		
    		placeDTO.getClientApplications().forEach(c -> {
    			
    			if(!this.clientApplicationRepository.existsById(c.getId())) {
    				throw new InvalidDataException("The ClientApplication "+c.getId()+" not exists.", "PLACE");
    			}
    			
    		});
    	}
    	
        this.cityRepository.findById(placeDTO.getCityId())
            .orElseThrow(() -> new InvalidDataException("The City "+placeDTO.getCityId()+" is invalid", "PLACE"));          
    }

    /**
     * Get all the places.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlaceSimpleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Places");
        
        return placeRepository.findAll(pageable)
        					  .map(placeMapper::toSimpleDto);
    }

    /**
     * Get all the Place with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<PlaceDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
    	
    	return placeRepository.findAllWithEagerRelationships(pageable);
    	
    }
    

    /**
     * Get one place by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceDTO> findOne(Long id) {
        log.debug("Request to get Place : {}", id);
        return placeRepository.findOneWithEagerRelationships(id)
                .map(placeMapper::toDto);
    }

    /**
     * Get one place with details by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceDetailsDTO> findOneDetails(Long id) {
        log.debug("Request to get Place : {}", id);
        return placeRepository.findOneWithEagerRelationshipsDetails(id);
    }
    

    /**
     * Get one place by slug.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceDetailsDTO> findOneBySlug(String slug) {
        log.debug("Request to get Place : {}", slug);
        return placeRepository.findOneBySlug(slug);
    }

    /**
     * Delete the place by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Place : {}", id);
        placeRepository.deleteById(id);
        placeSearchRepository.deleteById(id);
    }

    /**
     * Search for the place corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlaceDTO> search(String query, Pageable pageable) {
        log.debug(">> Request to search for a page of Places for query {}", queryStringQuery(query));
        return placeSearchRepository.search(queryStringQuery(query), pageable)
            .map(placeMapper::toDto);
    }

    /**
     * Rebuild de indexes for Places
     */
    @Transactional(readOnly = true)
    public void reindex() {

        log.debug("Reindexing ... ");

        // Isso deveria funcionar ????
        // placeSearchRepository.refresh();
        List<Place> list = placeRepository.findAll();

        list.forEach(p -> {
            this.placeSearchRepository.save(p);
        });
    }
}
