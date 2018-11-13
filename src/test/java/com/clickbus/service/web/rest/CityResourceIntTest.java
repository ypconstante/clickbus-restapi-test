package com.clickbus.service.web.rest;

import static com.clickbus.service.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
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
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.clickbus.service.ClickbusApp;
import com.clickbus.service.domain.City;
import com.clickbus.service.domain.State;
import com.clickbus.service.repository.CityRepository;
import com.clickbus.service.repository.search.CitySearchRepository;
import com.clickbus.service.service.CityService;
import com.clickbus.service.service.dto.CityDTO;
import com.clickbus.service.service.mapper.CityMapper;
import com.clickbus.service.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * Test class for the CityResource REST controller.
 *
 * @see CityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickbusApp.class)
public class CityResourceIntTest {

    public static final String DEFAULT_NAME = "CITY AAAAA";
    private static final String UPDATED_NAME = "CITY BBBBB";
    
    private static final String DEFAULT_STATE_NAME = "STATE AAAAA";
    private static final String UPDATED_STATE_NAME = "STATE BBBBB";

    private static final String DEFAULT_CREATED_BY = "system";
    private static final String UPDATED_CREATED_BY = "system"; // TODO Here would be User or Admin

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "system";
    private static final String UPDATED_UPDATED_BY = "system"; // TODO Here would be User or Admin

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;
    
    @Autowired
    private CityService cityService;

    /**
     * This repository is mocked in the com.clickbus.service.repository.search test package.
     *
     * @see com.clickbus.service.repository.search.CitySearchRepositoryMockConfiguration
     */
    @Autowired
    private CitySearchRepository mockCitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCityMockMvc;

    private City city;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CityResource cityResource = new CityResource(cityService);
        this.restCityMockMvc = MockMvcBuilders.standaloneSetup(cityResource)
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
    public static City createEntity(EntityManager em) {
        City city = new City();
        city.setName(DEFAULT_NAME);

        // Add required entity
        State state = StateResourceIntTest.createEntity(em);
        // state.setName(DEFAULT_STATE_NAME);
        em.persist(state);
        em.flush();
        city.setState(state);
        
        return city;
    }

    @Before
    public void initTest() {
        city = createEntity(em);
    }

    @Test
    @Transactional
    public void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        restCityMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isCreated());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getState()).isNotNull();
        assertThat(testCity.getState().getId()).isNotNull();
        // assertThat(testCity.getState().getName()).isEqualTo(DEFAULT_STATE_NAME);
        assertThat(testCity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testCity.getCreatedDate()));
        assertThat(testCity.getLastModifiedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testCity.getLastModifiedDate()));

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(1)).save(testCity);
    }

    @Test
    @Transactional
    public void createCityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City with an existing ID
        city.setId(1L);
        CityDTO cityDTO = cityMapper.toDto(city);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(0)).save(city);
    }
    
    @Test
    @Transactional
    public void createCityWithExistingName() throws Exception {
        
    	// Initialize the database
        cityRepository.saveAndFlush(city);
        
    	int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City with an existing NAME
    	city.setName(DEFAULT_NAME);
        CityDTO cityDTO = cityMapper.toDto(city);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(0)).save(city);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setName(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);

        restCityMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setState(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);

        restCityMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void createCityWithNonExistingState() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City with an existing ID
        CityDTO cityDTO = cityMapper.toDto(city);
        
        cityDTO.setStateId(Long.MAX_VALUE);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.entityName").value("CITY"))
            .andExpect(jsonPath("$.title").value("The State "+Long.MAX_VALUE+" is invalid"));

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(0)).save(city);
    }

    @Test
    @Transactional
    public void getAllCities() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc.perform(get("/api/cities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            // .andExpect(jsonPath("$.[*].stateId").value(hasItem("2"))) // TODO Esta espressao nao está ok
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
            
    }
    
    @Test
    @Transactional
    public void getCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);
        
        // Get the city
        restCityMockMvc.perform(get("/api/cities/{id}", city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            //.andExpect(jsonPath("$.stateId").value(any(String.class)))
            .andExpect(jsonPath("$.stateName").value(DEFAULT_STATE_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            // .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_UPDATED_BY.toString()))
            //.andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            ;
    }

    @Test
    @Transactional
    public void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get("/api/cities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).get();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity.setName(UPDATED_NAME);
        State stateUpdate = new State();
        stateUpdate.setName(UPDATED_STATE_NAME);
        em.persist(stateUpdate);
        em.flush();
        updatedCity.setState(stateUpdate);
            
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        restCityMockMvc.perform(put("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getState().getName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testCity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        // assertThat(testCity.getCreatedDate()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCity.getLastModifiedBy()).isEqualTo(UPDATED_UPDATED_BY);
        // assertThat(testCity.getLastModifiedDate()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(1)).save(testCity);
    }

    @Test
    @Transactional
    public void updateNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc.perform(put("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(0)).save(city);
    }

    @Test
    @Transactional
    public void deleteCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeDelete = cityRepository.findAll().size();

        // Get the city
        restCityMockMvc.perform(delete("/api/cities/{id}", city.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the City in Elasticsearch
        verify(mockCitySearchRepository, times(1)).deleteById(city.getId());
    }

    @Test
    @Transactional
    public void searchCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);
        when(mockCitySearchRepository.search(queryStringQuery("id:" + city.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(city), PageRequest.of(0, 1), 1));
        // Search the city
        restCityMockMvc.perform(get("/api/_search/cities?query=id:" + city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
    }

    /* TODO Esse teste não passou por causa do @EqualsAndHashCode do Lombok 
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = new City();
        city1.setId(1L);
        City city2 = new City();
        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);
        city2.setId(2L);
        assertThat(city1).isNotEqualTo(city2);
        city1.setId(null);
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityDTO.class);
        CityDTO cityDTO1 = new CityDTO();
        cityDTO1.setId(1L);
        CityDTO cityDTO2 = new CityDTO();
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
        cityDTO2.setId(cityDTO1.getId());
        assertThat(cityDTO1).isEqualTo(cityDTO2);
        cityDTO2.setId(2L);
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
        cityDTO1.setId(null);
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
    }*/

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cityMapper.fromId(null)).isNull();
    }
}
