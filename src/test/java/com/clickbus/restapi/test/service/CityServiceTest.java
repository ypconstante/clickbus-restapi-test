package com.clickbus.restapi.test.service;

import static org.junit.Assert.assertNotNull;

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

import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.repository.CityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CityServiceTest {

	@MockBean
	private CityRepository repository;

	@Autowired
	private CityService service;


	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.repository.save(Mockito.any(City.class))).willReturn(new City());
	}
	
	@Test
	public void testSave() {
		City c = this.service.save(new City());

		assertNotNull(c);
	}

}
