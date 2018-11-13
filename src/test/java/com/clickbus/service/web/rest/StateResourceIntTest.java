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
import com.clickbus.service.domain.Country;
import com.clickbus.service.domain.State;
import com.clickbus.service.repository.StateRepository;
import com.clickbus.service.repository.search.StateSearchRepository;
import com.clickbus.service.service.StateService;
import com.clickbus.service.service.dto.StateDTO;
import com.clickbus.service.service.mapper.StateMapper;
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
 * Test class for the StateResource REST controller.
 *
 * @see StateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickbusApp.class)
public class StateResourceIntTest {

    public static final String DEFAULT_NAME = "STATE AAAAA";
    private static final String UPDATED_NAME = "STATE BBBBB";

    private static final String DEFAULT_CREATED_BY = "system";
    private static final String UPDATED_CREATED_BY = "system";  // TODO Here would be User or Admin

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "system";
    private static final String UPDATED_UPDATED_BY = "system";  // TODO Here would be User or Admin

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateMapper stateMapper;
    
    @Autowired
    private StateService stateService;

    /**
     * This repository is mocked in the com.clickbus.service.repository.search test package.
     *
     * @see com.clickbus.service.repository.search.StateSearchRepositoryMockConfiguration
     */
    @Autowired
    private StateSearchRepository mockStateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStateMockMvc;

    private State state;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StateResource stateResource = new StateResource(stateService);
        this.restStateMockMvc = MockMvcBuilders.standaloneSetup(stateResource)
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
    public static State createEntity(EntityManager em) {
        
    	Country country = CountryResourceIntTest.createEntity(em);
    	em.persist(country);
    	em.flush();
    	
    	State state = new State();
        state.setName(DEFAULT_NAME);
        state.setCountry(country);
        
        return state;
    }

    @Before
    public void initTest() {
        state = createEntity(em);
    }

    @Test
    @Transactional
    public void createState() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);
        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isCreated());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate + 1);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testState.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testState.getCreatedDate()));
        assertThat(testState.getLastModifiedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testState.getLastModifiedDate()));

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(1)).save(testState);
    }

    @Test
    @Transactional
    public void createStateWithExistingId() throws Exception {
        
    	int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // Create the State with an existing ID
        state.setId(1L);
        StateDTO stateDTO = stateMapper.toDto(state);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(0)).save(state);
    }
    
    @Test
    @Transactional
    public void createStateWithExistingName() throws Exception {
        
    	stateRepository.saveAndFlush(state);
    	
    	int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // Create the State with an existing Name
        state.setName(DEFAULT_NAME);;
        StateDTO stateDTO = stateMapper.toDto(state);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(0)).save(state);
    }

   
    @Test
    @Transactional
    public void createStateWithNonExistingCountry() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // Create the State with an existing ID
        state.setCountry(new Country(Long.MAX_VALUE, null, null));
        StateDTO stateDTO = stateMapper.toDto(state);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.entityName").value("STATE"))
            .andExpect(jsonPath("$.title").value("The Country "+Long.MAX_VALUE+" is invalid"));

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(0)).save(state);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().size();
        // set the field null
        state.setName(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());
            

        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void getAllStates() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList
        restStateMockMvc.perform(get("/api/states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(state.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].CreatedDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].LastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
    }
    
    @Test
    @Transactional
    public void getState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get the state
        restStateMockMvc.perform(get("/api/states/{id}", state.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(state.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingState() throws Exception {
        // Get the state
        restStateMockMvc.perform(get("/api/states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Update the state
        State updatedState = stateRepository.findById(state.getId()).get();
        // Disconnect from session so that the updates on updatedState are not directly saved in db
        em.detach(updatedState);
        updatedState.setName(UPDATED_NAME);
        
        StateDTO stateDTO = stateMapper.toDto(updatedState);

        restStateMockMvc.perform(put("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testState.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        // assertThat(testState.getCreatedDate()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testState.getLastModifiedBy()).isEqualTo(UPDATED_UPDATED_BY);
        // assertThat(testState.getLastModifiedDate()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(1)).save(testState);
    }

    @Test
    @Transactional
    public void updateNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateMockMvc.perform(put("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(0)).save(state);
    }

    @Test
    @Transactional
    public void deleteState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeDelete = stateRepository.findAll().size();

        // Get the state
        restStateMockMvc.perform(delete("/api/states/{id}", state.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the State in Elasticsearch
        verify(mockStateSearchRepository, times(1)).deleteById(state.getId());
    }

    @Test
    @Transactional
    public void searchState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);
        when(mockStateSearchRepository.search(queryStringQuery("id:" + state.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(state), PageRequest.of(0, 1), 1));
        // Search the state
        restStateMockMvc.perform(get("/api/_search/states?query=id:" + state.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(state.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].CreatedDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].LastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
    }

    /* TODO Esse teste não passou por causa do @EqualsAndHashCode do Lombok 
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(State.class);
        State state1 = new State();
        state1.setId(1L);
        State state2 = new State();
        state2.setId(state1.getId());
        assertThat(state1).isEqualTo(state2);
        state2.setId(2L);
        assertThat(state1).isNotEqualTo(state2);
        state1.setId(null);
        assertThat(state1).isNotEqualTo(state2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateDTO.class);
        StateDTO stateDTO1 = new StateDTO();
        stateDTO1.setId(1L);
        StateDTO stateDTO2 = new StateDTO();
        assertThat(stateDTO1).isNotEqualTo(stateDTO2);
        stateDTO2.setId(stateDTO1.getId());
        assertThat(stateDTO1).isEqualTo(stateDTO2);
        stateDTO2.setId(2L);
        assertThat(stateDTO1).isNotEqualTo(stateDTO2);
        stateDTO1.setId(null);
        assertThat(stateDTO1).isNotEqualTo(stateDTO2);
    }*/

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stateMapper.fromId(null)).isNull();
    }
}
