package com.clickbus.restapi.test.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

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

import com.clickbus.restapi.test.entity.PlaceClientApplication;
import com.clickbus.restapi.test.repository.PlaceClientApplicationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PlaceClientApplicationServiceTest {

	@MockBean
	private PlaceClientApplicationRepository repository;

	@Autowired
	private PlaceClientApplicationService service;


	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.repository.save(Mockito.any(PlaceClientApplication.class))).willReturn(new PlaceClientApplication());
		BDDMockito.given(this.repository.findByPlaceIdIdOrderByClientIdId(Mockito.anyLong())).willReturn(new ArrayList<PlaceClientApplication>());
	}
	
	@Test
	public void testSave() {
		PlaceClientApplication p = this.service.save(new PlaceClientApplication());
		assertNotNull(p);
	}
	
	@Test
	public void testFindByPlaceId() {
		List<PlaceClientApplication> p = this.service.findByPlaceId(1L);
		assertNotNull(p);
	}

}
