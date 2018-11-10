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

import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.repository.CountryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CountryServiceTest {

	@MockBean
	private CountryRepository repository;

	@Autowired
	private CountryService service;


	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.repository.save(Mockito.any(Country.class))).willReturn(new Country());
	}
	
	@Test
	public void testPersistirEmpresa() {
		Country c = this.service.save(new Country());

		assertNotNull(c);
	}

}
