package com.clickbus.service.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.clickbus.service.service.ClientApplicationService;
import com.clickbus.service.service.PlaceService;
import com.clickbus.service.service.dto.ClientApplicationDTO;
import com.clickbus.service.service.dto.PlaceDTO;
import com.clickbus.service.service.dto.projections.PlaceDetailsDTO;
import com.clickbus.service.service.dto.projections.PlaceProjectionsDTO;
import com.clickbus.service.web.rest.errors.BadRequestAlertException;
import com.clickbus.service.web.rest.util.HeaderUtil;
import com.clickbus.service.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Place.
 */
@RestController
@RequestMapping("/api")
public class PlaceResource {

    private final Logger log = LoggerFactory.getLogger(PlaceResource.class);

    private static final String ENTITY_NAME = "place";

    private PlaceService placeService;
    
    private ClientApplicationService clientApplicationService;

    public PlaceResource(PlaceService placeService, ClientApplicationService clientApplicationService) {
        this.placeService = placeService;
        this.clientApplicationService = clientApplicationService;
    }

    /**
     * POST  /places : Create a new place.
     *
     * @param placeDTO the placeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new placeDTO, or with status 400 (Bad Request) if the place has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/places")
    @Timed
    public ResponseEntity<PlaceDTO> createPlace(@Valid @RequestBody PlaceDTO placeDTO) throws URISyntaxException {
        log.debug("REST request to save Place : {}", placeDTO);
        if (placeDTO.getId() != null) {
            throw new BadRequestAlertException("A new place cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlaceDTO result = placeService.save(placeDTO);
        return ResponseEntity.created(new URI("/api/places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /places : Updates an existing place.
     *
     * @param placeDTO the placeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated placeDTO,
     * or with status 400 (Bad Request) if the placeDTO is not valid,
     * or with status 500 (Internal Server Error) if the placeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/places")
    @Timed
    public ResponseEntity<PlaceDTO> updatePlace(@Valid @RequestBody PlaceDTO placeDTO) throws URISyntaxException {
        log.debug("REST request to update Place : {}", placeDTO);
        if (placeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlaceDTO result = placeService.save(placeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /places : get all the places.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of places in body
     */
    @GetMapping("/places")
    @Timed
    public ResponseEntity<List<? extends PlaceProjectionsDTO>> getAllPlaces(Pageable pageable, @RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get a page of Places");
        Page<? extends PlaceProjectionsDTO> page;
        if (eagerload) {
            page = placeService.findAllWithEagerRelationships(pageable);
        } else {
            page = placeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/places?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /places/:id : get the "id" place.
     *
     * @param id the id of the placeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the placeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/places/{id:[0-9]+}")
    @Timed
    public ResponseEntity<PlaceDTO> getPlace(@PathVariable Long id) {
        log.debug("REST request to get Place : {}", id);
        Optional<PlaceDTO> placeDTO = placeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(placeDTO);
    }
    
    /**
     * GET  /places/:id/details : get the "id" place with yours details like, country, city, state, clientIds.
     *
     * @param id the id of the placeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the placeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/places/{id:[0-9]+}/details")
    @Timed
    public ResponseEntity<PlaceDetailsDTO> getPlaceDetails(@PathVariable Long id) {
        log.debug("REST request to get Place : {}", id);
        Optional<PlaceDetailsDTO> placeDTO = placeService.findOneDetails(id);
        return ResponseUtil.wrapOrNotFound(placeDTO);
    }
    
    /**
     * GET  /places/:id/details : get the "id" place with yours details like, country, city, state, clientIds.
     *
     * @param id the id of the placeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the placeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/places/{id:[0-9]+}/clients")
    @Timed
    public ResponseEntity<List<ClientApplicationDTO>> getPlaceClients(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get Place : {}", id);
        
        //TODO Chamar servido do clientApplicationService
        
        Page<ClientApplicationDTO> page = clientApplicationService.findAllByPlace(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/places/%b/clients", id));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /places/:id : delete the "id" place.
     *
     * @param id the id of the placeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/places/{id}")
    @Timed
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        log.debug("REST request to delete Place : {}", id);
        placeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/places?query=:query : search for the place corresponding
     * to the query.
     *
     * @param query the query of the place search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/places")
    @Timed
    public ResponseEntity<List<PlaceDTO>> searchPlaces(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Places for query {}", query);
        Page<PlaceDTO> page = placeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    /**
     * SEARCH  /_search/places-slug/{slug} : search for the place corresponding
     * to the query.
     *
     * @param query the query of the place search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/places-slug/{slug}")
    @Timed
    public ResponseEntity<PlaceDetailsDTO> searchPlacesBySlug(@PathVariable String slug) {
        log.debug("REST request to search for a page of Places for query {}", slug);
        Optional<PlaceDetailsDTO> placeDTO = placeService.findOneBySlug(slug);
        return ResponseUtil.wrapOrNotFound(placeDTO);
    }


    @GetMapping("/places-reindex")
    @Timed
    public ResponseEntity<Void> reindexBase() {
        log.debug("REST request to reindex the search base");
        placeService.reindex();
        return ResponseEntity.ok().build();
    }


}
