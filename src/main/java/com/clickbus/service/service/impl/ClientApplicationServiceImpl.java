package com.clickbus.service.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clickbus.service.domain.ClientApplication;
import com.clickbus.service.repository.ClientApplicationRepository;
import com.clickbus.service.repository.PlaceRepository;
import com.clickbus.service.repository.search.ClientApplicationSearchRepository;
import com.clickbus.service.service.ClientApplicationService;
import com.clickbus.service.service.dto.ClientApplicationDTO;
import com.clickbus.service.service.mapper.ClientApplicationMapper;

/**
 * Service Implementation for managing ClientApplication.
 */
@Service
@Transactional
public class ClientApplicationServiceImpl implements ClientApplicationService {

    private final Logger log = LoggerFactory.getLogger(ClientApplicationServiceImpl.class);

    private ClientApplicationRepository clientApplicationRepository;
    
    private ClientApplicationMapper clientApplicationMapper;

    private ClientApplicationSearchRepository clientApplicationSearchRepository;

    public ClientApplicationServiceImpl(ClientApplicationRepository clientApplicationRepository,
                                        PlaceRepository placeRepository,
                                        ClientApplicationMapper clientApplicationMapper, 
                                        ClientApplicationSearchRepository clientApplicationSearchRepository) {
        this.clientApplicationRepository = clientApplicationRepository;
        this.clientApplicationMapper = clientApplicationMapper;
        this.clientApplicationSearchRepository = clientApplicationSearchRepository;
    }

    /**
     * Save a clientApplication.
     *
     * @param clientApplicationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClientApplicationDTO save(ClientApplicationDTO clientApplicationDTO) {
        log.debug("Request to save ClientApplication : {}", clientApplicationDTO);

        ClientApplication clientApplication = clientApplicationMapper.toEntity(clientApplicationDTO);
        clientApplication = clientApplicationRepository.save(clientApplication);
        ClientApplicationDTO result = clientApplicationMapper.toDto(clientApplication);
        clientApplicationSearchRepository.save(clientApplication);
        return result;
    }

    /**
     * Get all the clientApplications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClientApplications");
        return clientApplicationRepository.findAll(pageable)
            .map(clientApplicationMapper::toDto);
    }
    
    /**
     * Get all the clientApplications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientApplicationDTO> findAllByPlace(Long placeId, Pageable pageable) {
        log.debug("Request to get all ClientApplications");
        
        // TODO FAZER query !!!! 
        return clientApplicationRepository.findAllByPlaceId(placeId, pageable)
        								  .map(clientApplicationMapper::toDto);
        
    }


    /**
     * Get one clientApplication by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClientApplicationDTO> findOne(Long id) {
        log.debug("Request to get ClientApplication : {}", id);
        return clientApplicationRepository.findById(id)
            .map(clientApplicationMapper::toDto);
    }

    /**
     * Delete the clientApplication by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClientApplication : {}", id);
        clientApplicationRepository.deleteById(id);
        clientApplicationSearchRepository.deleteById(id);
    }

    /**
     * Search for the clientApplication corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientApplicationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClientApplications for query {}", query);
        return clientApplicationSearchRepository.search(queryStringQuery(query), pageable)
            .map(clientApplicationMapper::toDto);
    }
}
