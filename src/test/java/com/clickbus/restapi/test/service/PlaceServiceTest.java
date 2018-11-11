package com.clickbus.restapi.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.repository.PlaceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PlaceServiceTest {

	@MockBean
	private PlaceRepository repository;

	@Autowired
	private PlaceService service;


	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.repository.save(Mockito.any(Place.class))).willReturn(new Place());
		BDDMockito.given(this.repository.findById(Mockito.anyLong())).willReturn(Optional.of(new Place()));
		BDDMockito.given(this.repository.findBySlugLikeIgnoreCase(Mockito.anyString())).willReturn(new ArrayList<Place>());
		BDDMockito.given(this.repository.findBySlugIgnoreCase(Mockito.anyString())).willReturn(Optional.of(new Place()));
		BDDMockito.given(this.repository.findAllPlaces()).willReturn(new ArrayList<Object[]>());
	}
	
	@Test
	public void testSave() {
		Place p = this.service.save(new Place());
		assertNotNull(p);
	}
	
	@Test
	public void testFindById() {
		Optional<Place> p = this.service.findById(1L);
		assertEquals(p.isPresent(), true);
	}
	
	@Test
	public void testFindBySlugLikeIgnoreCase() {
		List<Place> p = this.service.findBySlugLikeIgnoreCase("");
		assertNotNull(p);
	}

	@Test
	public void testFindBySlugIgnoreCase() {
		Optional<Place> p = this.service.findBySlugIgnoreCase("");
		assertEquals(p.isPresent(), true);
	}
	
	@Test
	public void testFindAllPlaces() {
		List<Object[]> l = this.service.findAllPlaces();
		assertNotNull(l);
	}
}
