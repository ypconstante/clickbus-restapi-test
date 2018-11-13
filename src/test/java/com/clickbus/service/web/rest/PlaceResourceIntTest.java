package com.clickbus.service.web.rest;

import static com.clickbus.service.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.clickbus.service.ClickbusApp;
import com.clickbus.service.domain.City;
import com.clickbus.service.domain.ClientApplication;
import com.clickbus.service.domain.Place;
import com.clickbus.service.repository.PlaceRepository;
import com.clickbus.service.repository.search.PlaceSearchRepository;
import com.clickbus.service.service.ClientApplicationService;
import com.clickbus.service.service.PlaceService;
import com.clickbus.service.service.dto.ClientApplicationDTO;
import com.clickbus.service.service.dto.PlaceDTO;
import com.clickbus.service.service.mapper.PlaceMapper;
import com.clickbus.service.web.rest.errors.ExceptionTranslator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the PlaceResource REST controller.
 *
 * @see PlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickbusApp.class)
public class PlaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";
    
    private static final String DEFAULT_PUBLIC_NAME = "AAAAAAAAAA";

    private static final String DEFAULT_TERMINAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "system";
    private static final String UPDATED_CREATED_BY = "system";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "system";
    private static final String UPDATED_UPDATED_BY = "system";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PlaceRepository placeRepository;

    @Mock
    private PlaceRepository placeRepositoryMock;

    @Autowired
    private PlaceMapper placeMapper;
    

    @Mock
    private PlaceService placeServiceMock;

    @Autowired
    private PlaceService placeService;
    
    @Autowired
    private ClientApplicationService clientApplicationService;

    /**
     * This repository is mocked in the com.clickbus.service.repository.search test package.
     *
     * @see com.clickbus.service.repository.search.PlaceSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlaceSearchRepository mockPlaceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlaceMockMvc;

    private Place place;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlaceResource placeResource = new PlaceResource(placeService, clientApplicationService);
        this.restPlaceMockMvc = MockMvcBuilders.standaloneSetup(placeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createEntity(EntityManager em) {
        Place place = new Place();
        place.setName(DEFAULT_NAME);
        place.setTerminalName(DEFAULT_TERMINAL_NAME);
        place.setAddress(DEFAULT_ADDRESS);
        place.setSlug(DEFAULT_SLUG);
        
        // Add required entity
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        place.setCity(city);
        
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setName(DEFAULT_NAME);
        clientApplication.setPublicName(DEFAULT_PUBLIC_NAME);
        em.persist(clientApplication);
        em.flush();
        place.getClientApplications().add(new ClientApplication(clientApplication.getId()));
        
        ClientApplication clientApplication2 = new ClientApplication();
        clientApplication2.setName(DEFAULT_NAME+"2");
        clientApplication2.setPublicName(DEFAULT_PUBLIC_NAME+"2");
        em.persist(clientApplication2);
        em.flush();
        place.getClientApplications().add(new ClientApplication(clientApplication2.getId()));
        
        
        return place;
    }

    @Before
    public void initTest() {
        place = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlace() throws Exception {
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isCreated());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate + 1);
        Place testPlace = placeList.get(placeList.size() - 1);
        
        assertThat(testPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlace.getTerminalName()).isEqualTo(DEFAULT_TERMINAL_NAME);
        assertThat(testPlace.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPlace.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testPlace.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPlace.getLastModifiedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(1)).save(testPlace);
    }

    @Test
    @Transactional
    public void createPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place with an existing ID
        place.setId(1L);
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(0)).save(place);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = placeRepository.findAll().size();
        // set the field null
        place.setName(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTerminalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = placeRepository.findAll().size();
        // set the field null
        place.setTerminalName(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = placeRepository.findAll().size();
        // set the field null
        place.setAddress(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSlugIsRequiredNull() throws Exception {

    	// set the field null
        place.setSlug(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.slug").value(DEFAULT_NAME.toLowerCase()));

    }
    
    @Test
    @Transactional
    public void checkSlugIsRequiredNullComplexName() throws Exception {
        
        // set the field null
        place.setSlug(null);
        place.setName("Nome para criação do slug");

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.slug").value("nome-para-criacao-do-slug"));

    }
    
    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        
        // set the field null
        place.setSlug("my-custom-slug");

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.slug").value("my-custom-slug"));

    }
    
    @Test
    @Transactional
    public void createPlaceWithExistingSlug() throws Exception {
    	
    	// Initialize the database
        placeRepository.saveAndFlush(place);
    	
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place with an existing SLUG
        place.setSlug(DEFAULT_SLUG);
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(0)).save(place);
    }
    
    @Test
    @Transactional
    public void createPlaceWithNonExistingCity() throws Exception {
    	
    	// Initialize the database
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place with an existing SLUG
        PlaceDTO placeDTO = placeMapper.toDto(place);
        placeDTO.setCityId(Long.MAX_VALUE);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.entityName").value("PLACE"))
            .andExpect(jsonPath("$.title").value("The City "+Long.MAX_VALUE+" is invalid"));

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(0)).save(place);
    }
    
    @Test
    @Transactional
    public void createPlaceWithNonExistingClientApplication() throws Exception {
    	
    	// Initialize the database
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place with an existing SLUG
        PlaceDTO placeDTO = placeMapper.toDto(place);
        placeDTO.getClientApplications().add(new ClientApplicationDTO(Long.MAX_VALUE, "", ""));

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.entityName").value("PLACE"))
            .andExpect(jsonPath("$.title").value("The ClientApplication "+Long.MAX_VALUE+" not exists."));

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(0)).save(place);
    }


    @Test
    @Transactional
    public void getAllPlaces() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList
        restPlaceMockMvc.perform(get("/api/places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].city.name").value(hasItem(CityResourceIntTest.DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].state.name").value(hasItem(StateResourceIntTest.DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].country.name").value(hasItem(CountryResourceIntTest.DEFAULT_NAME)))
            ;
    }
    
    @Test
    @Transactional
    public void getAllPlacesEagerFalse() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList
        restPlaceMockMvc.perform(get("/api/places?eagerload=false&sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].terminalName").value(hasItem(DEFAULT_TERMINAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            ;
    }
    
    public void getAllPlacesWithEagerRelationshipsIsEnabled() throws Exception {
        PlaceResource placeResource = new PlaceResource(placeServiceMock, clientApplicationService);
        when(placeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPlaceMockMvc = MockMvcBuilders.standaloneSetup(placeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlaceMockMvc.perform(get("/api/places?eagerload=true"))
        .andExpect(status().isOk());

        verify(placeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllPlacesWithEagerRelationshipsIsNotEnabled() throws Exception {
        PlaceResource placeResource = new PlaceResource(placeServiceMock, clientApplicationService);
            when(placeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPlaceMockMvc = MockMvcBuilders.standaloneSetup(placeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlaceMockMvc.perform(get("/api/places?eagerload=true"))
        .andExpect(status().isOk());

            verify(placeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);
        
        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}", place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.terminalName").value(DEFAULT_TERMINAL_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_UPDATED_BY.toString()))
            ;
    }
    
    @Test
    @Transactional
    public void getPlaceDetails() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}/details", place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.city.name").value(CityResourceIntTest.DEFAULT_NAME))
            .andExpect(jsonPath("$.state.name").value(StateResourceIntTest.DEFAULT_NAME))
            .andExpect(jsonPath("$.country.name").value(CountryResourceIntTest.DEFAULT_NAME))
            .andExpect(jsonPath("$.clientIds", Matchers.hasSize(2)))
            ;
    }
    
    @Test
    @Transactional
    public void getPlaceClients() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}/clients", place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getClientApplications().iterator().next().getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].publicName").value(hasItem(DEFAULT_PUBLIC_NAME.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME + "2")))
            .andExpect(jsonPath("$.[*].publicName").value(hasItem(DEFAULT_PUBLIC_NAME + "2")))
            ;
    }

    @Test
    @Transactional
    public void getNonExistingPlace() throws Exception {
    	
        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Update the place
        Place updatedPlace = placeRepository.findById(place.getId()).get();
        // Disconnect from session so that the updates on updatedPlace are not directly saved in db
        em.detach(updatedPlace);
        updatedPlace.setName(UPDATED_NAME);
        updatedPlace.setTerminalName(UPDATED_TERMINAL_NAME);
        updatedPlace.setAddress(UPDATED_ADDRESS);
        updatedPlace.setSlug(UPDATED_SLUG);
        
        PlaceDTO placeDTO = placeMapper.toDto(updatedPlace);

        restPlaceMockMvc.perform(put("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isOk());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlace.getTerminalName()).isEqualTo(UPDATED_TERMINAL_NAME);
        assertThat(testPlace.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPlace.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testPlace.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testPlace.getCreatedDate()));
        assertThat(testPlace.getLastModifiedBy()).isEqualTo(UPDATED_UPDATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testPlace.getLastModifiedDate()));

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(1)).save(testPlace);
    }

    @Test
    @Transactional
    public void updateNonExistingPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceMockMvc.perform(put("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(0)).save(place);
    }

    @Test
    @Transactional
    public void deletePlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        int databaseSizeBeforeDelete = placeRepository.findAll().size();

        // Get the place
        restPlaceMockMvc.perform(delete("/api/places/{id}", place.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Place in Elasticsearch
        verify(mockPlaceSearchRepository, times(1)).deleteById(place.getId());
    }

    @Test
    @Transactional
    public void searchPlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);
        when(mockPlaceSearchRepository.search(queryStringQuery("id:" + place.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(place), PageRequest.of(0, 1), 1));
        // Search the place
        restPlaceMockMvc.perform(get("/api/_search/places?query=id:" + place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].terminalName").value(hasItem(DEFAULT_TERMINAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
    }
    
    @Test
    @Transactional
    public void searchPlaceBySlug() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);
        
        // Search the place
        restPlaceMockMvc.perform(get("/api/_search/places-slug/{slug}", place.getSlug()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.city.name").value(CityResourceIntTest.DEFAULT_NAME))
            .andExpect(jsonPath("$.state.name").value(StateResourceIntTest.DEFAULT_NAME))
            .andExpect(jsonPath("$.country.name").value(CountryResourceIntTest.DEFAULT_NAME))
            .andExpect(jsonPath("$.clientIds", Matchers.hasSize(2)))
            ;
    }
    
    @Test
    @Transactional
    public void searchPlaceByNonExistingSlug() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);
        
        // Search the place
        restPlaceMockMvc.perform(get("/api/_search/places-slug/{slug}", "NONEXISTING"))
        				.andExpect(status().isNotFound());
    }
    
   /* @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Place.class);
        Place place1 = new Place();
        place1.setId(1L);
        Place place2 = new Place();
        place2.setId(place1.getId());
        assertThat(place1).isEqualTo(place2);
        place2.setId(2L);
        assertThat(place1).isNotEqualTo(place2);
        place1.setId(null);
        assertThat(place1).isNotEqualTo(place2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaceDTO.class);
        PlaceDTO placeDTO1 = new PlaceDTO();
        placeDTO1.setId(1L);
        PlaceDTO placeDTO2 = new PlaceDTO();
        assertThat(placeDTO1).isNotEqualTo(placeDTO2);
        placeDTO2.setId(placeDTO1.getId());
        assertThat(placeDTO1).isEqualTo(placeDTO2);
        placeDTO2.setId(2L);
        assertThat(placeDTO1).isNotEqualTo(placeDTO2);
        placeDTO1.setId(null);
        assertThat(placeDTO1).isNotEqualTo(placeDTO2);
    }*/

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(placeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(placeMapper.fromId(null)).isNull();
    }
}
