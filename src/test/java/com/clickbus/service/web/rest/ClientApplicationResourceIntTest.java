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
import com.clickbus.service.domain.ClientApplication;
import com.clickbus.service.repository.ClientApplicationRepository;
import com.clickbus.service.repository.search.ClientApplicationSearchRepository;
import com.clickbus.service.service.ClientApplicationService;
import com.clickbus.service.service.dto.ClientApplicationDTO;
import com.clickbus.service.service.mapper.ClientApplicationMapper;
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
 * Test class for the ClientApplicationResource REST controller.
 *
 * @see ClientApplicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickbusApp.class)
public class ClientApplicationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PUBLIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "system";
    private static final String UPDATED_CREATED_BY = "system";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "system";
    private static final String UPDATED_UPDATED_BY = "system";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ClientApplicationRepository clientApplicationRepository;

    @Autowired
    private ClientApplicationMapper clientApplicationMapper;
    
    @Autowired
    private ClientApplicationService clientApplicationService;

    /**
     * This repository is mocked in the com.clickbus.service.repository.search test package.
     *
     * @see com.clickbus.service.repository.search.ClientApplicationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ClientApplicationSearchRepository mockClientApplicationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClientApplicationMockMvc;

    private ClientApplication clientApplication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientApplicationResource clientApplicationResource = new ClientApplicationResource(clientApplicationService);
        this.restClientApplicationMockMvc = MockMvcBuilders.standaloneSetup(clientApplicationResource)
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
    public static ClientApplication createEntity(EntityManager em) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setName(DEFAULT_NAME);
        clientApplication.setPublicName(DEFAULT_PUBLIC_NAME);
        
        return clientApplication;
    }

    @Before
    public void initTest() {
        clientApplication = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientApplication() throws Exception {
        int databaseSizeBeforeCreate = clientApplicationRepository.findAll().size();

        // Create the ClientApplication
        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(clientApplication);
        restClientApplicationMockMvc.perform(post("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isCreated());

        // Validate the ClientApplication in the database
        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        ClientApplication testClientApplication = clientApplicationList.get(clientApplicationList.size() - 1);
        assertThat(testClientApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClientApplication.getPublicName()).isEqualTo(DEFAULT_PUBLIC_NAME);
        assertThat(testClientApplication.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testClientApplication.getCreatedDate()));
        assertThat(testClientApplication.getLastModifiedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testClientApplication.getLastModifiedDate()));

        // Validate the ClientApplication in Elasticsearch
        verify(mockClientApplicationSearchRepository, times(1)).save(testClientApplication);
    }

    @Test
    @Transactional
    public void createClientApplicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientApplicationRepository.findAll().size();

        // Create the ClientApplication with an existing ID
        clientApplication.setId(1L);
        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(clientApplication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientApplicationMockMvc.perform(post("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientApplication in the database
        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ClientApplication in Elasticsearch
        verify(mockClientApplicationSearchRepository, times(0)).save(clientApplication);
    }
    
    @Test
    @Transactional
    public void createClientApplicationWithExistingName() throws Exception {
    	
    	clientApplicationRepository.saveAndFlush(clientApplication);
    	
        int databaseSizeBeforeCreate = clientApplicationRepository.findAll().size();

        // Create the ClientApplication with an existing Name
        clientApplication.setName(DEFAULT_NAME);
        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(clientApplication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientApplicationMockMvc.perform(post("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientApplication in the database
        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ClientApplication in Elasticsearch
        verify(mockClientApplicationSearchRepository, times(0)).save(clientApplication);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientApplicationRepository.findAll().size();
        // set the field null
        clientApplication.setName(null);

        // Create the ClientApplication, which fails.
        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(clientApplication);

        restClientApplicationMockMvc.perform(post("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isBadRequest());

        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublicNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientApplicationRepository.findAll().size();
        // set the field null
        clientApplication.setPublicName(null);

        // Create the ClientApplication, which fails.
        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(clientApplication);

        restClientApplicationMockMvc.perform(post("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isBadRequest());

        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeTest);
    }


    @Test
    @Transactional
    public void getAllClientApplications() throws Exception {
        // Initialize the database
        clientApplicationRepository.saveAndFlush(clientApplication);

        // Get all the clientApplicationList
        restClientApplicationMockMvc.perform(get("/api/client-applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].publicName").value(hasItem(DEFAULT_PUBLIC_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
    }
    
    @Test
    @Transactional
    public void getClientApplication() throws Exception {
        // Initialize the database
        clientApplicationRepository.saveAndFlush(clientApplication);

        // Get the clientApplication
        restClientApplicationMockMvc.perform(get("/api/client-applications/{id}", clientApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientApplication.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.publicName").value(DEFAULT_PUBLIC_NAME.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            // .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_UPDATED_BY.toString()))
            // .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            ;
    }

    @Test
    @Transactional
    public void getNonExistingClientApplication() throws Exception {
        // Get the clientApplication
        restClientApplicationMockMvc.perform(get("/api/client-applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientApplication() throws Exception {
        // Initialize the database
        clientApplicationRepository.saveAndFlush(clientApplication);

        int databaseSizeBeforeUpdate = clientApplicationRepository.findAll().size();

        // Update the clientApplication
        ClientApplication updatedClientApplication = clientApplicationRepository.findById(clientApplication.getId()).get();
        // Disconnect from session so that the updates on updatedClientApplication are not directly saved in db
        em.detach(updatedClientApplication);
        updatedClientApplication.setName(UPDATED_NAME);
        updatedClientApplication.setPublicName(UPDATED_PUBLIC_NAME);

        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(updatedClientApplication);

        restClientApplicationMockMvc.perform(put("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isOk());

        // Validate the ClientApplication in the database
        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeUpdate);
        ClientApplication testClientApplication = clientApplicationList.get(clientApplicationList.size() - 1);
        assertThat(testClientApplication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClientApplication.getPublicName()).isEqualTo(UPDATED_PUBLIC_NAME);
        assertThat(testClientApplication.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testClientApplication.getCreatedDate()));
        assertThat(testClientApplication.getLastModifiedBy()).isEqualTo(UPDATED_UPDATED_BY);
        // assertTrue(TestUtil.compareDatesMinutes(testClientApplication.getLastModifiedDate()));

        // Validate the ClientApplication in Elasticsearch
        verify(mockClientApplicationSearchRepository, times(1)).save(testClientApplication);
    }

    @Test
    @Transactional
    public void updateNonExistingClientApplication() throws Exception {
        int databaseSizeBeforeUpdate = clientApplicationRepository.findAll().size();

        // Create the ClientApplication
        ClientApplicationDTO clientApplicationDTO = clientApplicationMapper.toDto(clientApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientApplicationMockMvc.perform(put("/api/client-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientApplicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientApplication in the database
        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ClientApplication in Elasticsearch
        verify(mockClientApplicationSearchRepository, times(0)).save(clientApplication);
    }

    @Test
    @Transactional
    public void deleteClientApplication() throws Exception {
        // Initialize the database
        clientApplicationRepository.saveAndFlush(clientApplication);

        int databaseSizeBeforeDelete = clientApplicationRepository.findAll().size();

        // Get the clientApplication
        restClientApplicationMockMvc.perform(delete("/api/client-applications/{id}", clientApplication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientApplication> clientApplicationList = clientApplicationRepository.findAll();
        assertThat(clientApplicationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ClientApplication in Elasticsearch
        verify(mockClientApplicationSearchRepository, times(1)).deleteById(clientApplication.getId());
    }

    @Test
    @Transactional
    public void searchClientApplication() throws Exception {
        // Initialize the database
        clientApplicationRepository.saveAndFlush(clientApplication);
        when(mockClientApplicationSearchRepository.search(queryStringQuery("id:" + clientApplication.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(clientApplication), PageRequest.of(0, 1), 1));
        // Search the clientApplication
        restClientApplicationMockMvc.perform(get("/api/_search/client-applications?query=id:" + clientApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].publicName").value(hasItem(DEFAULT_PUBLIC_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())))
            // .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            ;
    }

/*    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientApplication.class);
        ClientApplication clientApplication1 = new ClientApplication();
        clientApplication1.setId(1L);
        ClientApplication clientApplication2 = new ClientApplication();
        clientApplication2.setId(clientApplication1.getId());
        assertThat(clientApplication1).isEqualTo(clientApplication2);
        clientApplication2.setId(2L);
        assertThat(clientApplication1).isNotEqualTo(clientApplication2);
        clientApplication1.setId(null);
        assertThat(clientApplication1).isNotEqualTo(clientApplication2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientApplicationDTO.class);
        ClientApplicationDTO clientApplicationDTO1 = new ClientApplicationDTO();
        clientApplicationDTO1.setId(1L);
        ClientApplicationDTO clientApplicationDTO2 = new ClientApplicationDTO();
        assertThat(clientApplicationDTO1).isNotEqualTo(clientApplicationDTO2);
        clientApplicationDTO2.setId(clientApplicationDTO1.getId());
        assertThat(clientApplicationDTO1).isEqualTo(clientApplicationDTO2);
        clientApplicationDTO2.setId(2L);
        assertThat(clientApplicationDTO1).isNotEqualTo(clientApplicationDTO2);
        clientApplicationDTO1.setId(null);
        assertThat(clientApplicationDTO1).isNotEqualTo(clientApplicationDTO2);
    }*/

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clientApplicationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clientApplicationMapper.fromId(null)).isNull();
    }
}
