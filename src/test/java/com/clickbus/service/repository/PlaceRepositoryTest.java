package com.clickbus.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.clickbus.service.ClickbusApp;
import com.clickbus.service.domain.City;
import com.clickbus.service.domain.ClientApplication;
import com.clickbus.service.domain.Place;
import com.clickbus.service.service.dto.projections.PlaceDetailsDTO;
import com.clickbus.service.web.rest.CityResourceIntTest;
import com.clickbus.service.web.rest.CountryResourceIntTest;
import com.clickbus.service.web.rest.StateResourceIntTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickbusApp.class)
@Transactional
public class PlaceRepositoryTest {
	
	private static final String DEFAULT_NAME = "PLACE AAAAAAAAAA";
	private static final String DEFAULT_TERMINAL_NAME = "PLACE AAAAAAAAAA";
	private static final String DEFAULT_ADDRESS = "PLACE AAAAAAAAAA";
    private static final String DEFAULT_SLUG = "PLACE AAAAAAAAAA";
    private static final String DEFAULT_PUBLIC_NAME = "PLACE AAAAAAAAAA";

    @Autowired
    private EntityManager em;
	
	@Autowired
	private PlaceRepository repository;
	
	private Place place;

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
        //place.getClientApplications().add(new ClientApplication(clientApplication.getId()));
        place.getClientApplications().add(clientApplication);
        
        ClientApplication clientApplication2 = new ClientApplication();
        clientApplication2.setName(DEFAULT_NAME+"2");
        clientApplication2.setPublicName(DEFAULT_PUBLIC_NAME+"2");
        em.persist(clientApplication2);
        em.flush();
        place.getClientApplications().add(clientApplication2);
        
        em.persist(place);
        
        return place;
    }

    @Before
    public void initTest() {
        place = createEntity(em);
    }
	
	/*@Test
	public void testFindAll() {
		assertThat(this.repository.findAll().size(), is(equalTo(1)));
	}*/
	
	
	@Test
	public void testFindAllWithEagerRelationshipsPageable() throws Exception {
		
		PlaceDetailsDTO testPlace = this.repository.findOneWithEagerRelationshipsDetails(this.place.getId()).orElseThrow(() -> new Exception());
		
		assertThat(testPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlace.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testPlace.getCity().getName()).isEqualTo(CityResourceIntTest.DEFAULT_NAME);
        assertThat(testPlace.getState().getName()).isEqualTo(StateResourceIntTest.DEFAULT_NAME);
        assertThat(testPlace.getCountry().getName()).isEqualTo(CountryResourceIntTest.DEFAULT_NAME);
		
		assertThat(testPlace.getClientIds().size()).isEqualTo(2);
	}
/*
	@Test
	public void testFindAllWithEagerRelationships() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindOneWithEagerRelationships() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindOneWithEagerRelationshipsDetails() {
		fail("Not yet implemented");
	}*/

}
