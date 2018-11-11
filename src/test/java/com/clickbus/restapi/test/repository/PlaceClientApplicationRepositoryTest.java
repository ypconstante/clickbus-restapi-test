package com.clickbus.restapi.test.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.entity.PlaceClientApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PlaceClientApplicationRepositoryTest {

	@Autowired
	private PlaceClientApplicationRepository repository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private ClientApplicationRepository clientRepository;

	private Long ID = 0L;

	@Before
	public void setUp() throws Exception {
		Place place = this.placeRepository.save(this.getPlace());
		ClientApplication c = this.clientRepository.save(this.getClient());
		PlaceClientApplication p = new PlaceClientApplication();
		p.setClientId(c);
		p.setPlaceId(place);
		this.repository.save(p);
		ID = place.getId();
	}

	@After
	public final void tearDown() {
		this.repository.deleteAll();
		this.placeRepository.deleteAll();
		this.clientRepository.deleteAll();
	}

	@Test
	public void testFindByPlaceId() {
		List<PlaceClientApplication> p = this.repository.findByPlaceIdIdOrderByClientIdId(ID);
		Long placeId = p.get(0).getPlaceId().getId();
		assertEquals(ID, placeId);
	}

	private Place getPlace() {
		Place p = new Place();
		p.setAdress("Adress");
		p.setName("Name");
		p.setSlug("Slug");
		p.setTerminalName("Teste");
		return p;
	}

	private ClientApplication getClient() {
		ClientApplication c = new ClientApplication();
		c.setName("Teste");
		c.setPublicName("Public name");
		return c;
	}

}
