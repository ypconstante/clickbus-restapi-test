package com.clickbus.service.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.clickbus.service.service.ClientApplicationService;
import com.clickbus.service.service.dto.ClientApplicationDTO;
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
 * REST controller for managing ClientApplication.
 */
@RestController
@RequestMapping("/api")
public class ClientApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ClientApplicationResource.class);

    private static final String ENTITY_NAME = "clientApplication";

    private ClientApplicationService clientApplicationService;

    public ClientApplicationResource(ClientApplicationService clientApplicationService) {
        this.clientApplicationService = clientApplicationService;
    }

    /**
     * POST  /client-applications : Create a new clientApplication.
     *
     * @param clientApplicationDTO the clientApplicationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientApplicationDTO, or with status 400 (Bad Request) if the clientApplication has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/client-applications")
    @Timed
    public ResponseEntity<ClientApplicationDTO> createClientApplication(@Valid @RequestBody ClientApplicationDTO clientApplicationDTO) throws URISyntaxException {
        log.debug("REST request to save ClientApplication : {}", clientApplicationDTO);
        if (clientApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientApplicationDTO result = clientApplicationService.save(clientApplicationDTO);
        return ResponseEntity.created(new URI("/api/client-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-applications : Updates an existing clientApplication.
     *
     * @param clientApplicationDTO the clientApplicationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientApplicationDTO,
     * or with status 400 (Bad Request) if the clientApplicationDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientApplicationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/client-applications")
    @Timed
    public ResponseEntity<ClientApplicationDTO> updateClientApplication(@Valid @RequestBody ClientApplicationDTO clientApplicationDTO) throws URISyntaxException {
        log.debug("REST request to update ClientApplication : {}", clientApplicationDTO);
        if (clientApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClientApplicationDTO result = clientApplicationService.save(clientApplicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clientApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-applications : get all the clientApplications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientApplications in body
     */
    @GetMapping("/client-applications")
    @Timed
    public ResponseEntity<List<ClientApplicationDTO>> getAllClientApplications(Pageable pageable) {
        log.debug("REST request to get a page of ClientApplications");
        Page<ClientApplicationDTO> page = clientApplicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-applications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-applications/:id : get the "id" clientApplication.
     *
     * @param id the id of the clientApplicationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientApplicationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/client-applications/{id}")
    @Timed
    public ResponseEntity<ClientApplicationDTO> getClientApplication(@PathVariable Long id) {
        log.debug("REST request to get ClientApplication : {}", id);
        Optional<ClientApplicationDTO> clientApplicationDTO = clientApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientApplicationDTO);
    }

    /**
     * DELETE  /client-applications/:id : delete the "id" clientApplication.
     *
     * @param id the id of the clientApplicationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/client-applications/{id}")
    @Timed
    public ResponseEntity<Void> deleteClientApplication(@PathVariable Long id) {
        log.debug("REST request to delete ClientApplication : {}", id);
        clientApplicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/client-applications?query=:query : search for the clientApplication corresponding
     * to the query.
     *
     * @param query the query of the clientApplication search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/client-applications")
    @Timed
    public ResponseEntity<List<ClientApplicationDTO>> searchClientApplications(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ClientApplications for query {}", query);
        Page<ClientApplicationDTO> page = clientApplicationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/client-applications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
