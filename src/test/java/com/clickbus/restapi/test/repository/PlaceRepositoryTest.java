package com.clickbus.restapi.test.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.entity.State;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PlaceRepositoryTest {

	@Autowired
	public PlaceRepository placeRepository;
	@Autowired
	public CityRepository cityRepository;
	@Autowired
	public StateRepository stateRepository;
	@Autowired
	public CountryRepository countryRepository;
	
	public static final String PLACENAME = "Place Name";
	public static final String CITYNAME = "City Name";
	public static final String SLUG = "Slug Name";
	public static final String COUNTRYNAME = "Country Name";
	public static final String STATENAME = "State Name";
	public Long IDPLACE = 0L;

	@Before
	public void setUp() throws Exception {
		Country country = this.countryRepository.save(this.getCountry());
		State state = this.stateRepository.save(this.getState(country.getId()));
		City c = this.cityRepository.save(this.getCity(state.getId()));
		Place place = this.placeRepository.save(this.getPlace(c.getId()));
		IDPLACE = place.getId();
	}

	@After
	public final void tearDown() {
		this.placeRepository.deleteAll();
		this.cityRepository.deleteAll();
		this.stateRepository.deleteAll();
		this.countryRepository.deleteAll();
	}

	@Test
	public void testFindBySlugLikeIgnoreCase() {
		List<Place> list = this.placeRepository.findBySlugLikeIgnoreCase(SLUG);
		assertEquals(list.size(), 1);
	}
	
	@Test
	public void testFindBySlugIgnoreCase() {
		Optional<Place> p = this.placeRepository.findBySlugIgnoreCase(SLUG);
		assertEquals(p.isPresent(), true);
	}
	
	@Test
	public void testFindAllPlaces() {
		List<Object[]> list = this.placeRepository.findAllPlaces();
		assertEquals(list.get(0)[0], IDPLACE);
		assertEquals(list.get(0)[1], PLACENAME);
		assertEquals(list.get(0)[2], SLUG);
		assertEquals(list.get(0)[3], CITYNAME);
		assertEquals(list.get(0)[4], STATENAME);
		assertEquals(list.get(0)[5], COUNTRYNAME);
	}

	private Place getPlace(Long cityId) {
		Place p = new Place();
		p.setAdress("Adress");
		p.setName(PLACENAME);
		p.setSlug(SLUG);
		p.setTerminalName("Teste");
		p.setCityId(new City());
		p.getCityId().setId(cityId);
		return p;
	}

	private City getCity(Long stateId) {
		City c = new City();
		c.setName(CITYNAME);
		c.setStateId(new State());
		c.getStateId().setId(stateId);
		return c;
	}
	
	private Country getCountry() {
		Country c = new Country();
		c.setName(COUNTRYNAME);
		return c;
	}
	
	private State getState(Long countryId)  {
		State s = new State();
		s.setName(STATENAME);
		s.setCountryId(new Country());
		s.getCountryId().setId(countryId);
		return s;
	}

}
