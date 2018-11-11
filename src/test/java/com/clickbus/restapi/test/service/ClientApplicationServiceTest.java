package com.clickbus.restapi.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.repository.ClientApplicationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClientApplicationServiceTest {

	@MockBean
	private ClientApplicationRepository repository;

	@Autowired
	private ClientApplicationService service;


	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.repository.save(Mockito.any(ClientApplication.class))).willReturn(new ClientApplication());
		BDDMockito.given(this.repository.findById(Mockito.anyLong())).willReturn(Optional.of(new ClientApplication()));
	}
	
	@Test
	public void testSave() {
		ClientApplication c = this.service.save(new ClientApplication());
		
		assertNotNull(c);
	}
	
	@Test
	public void testFindById() {
		Optional<ClientApplication> c = this.service.findById(1L);
		assertEquals(c.isPresent(), true);
	}

}
