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

import com.clickbus.restapi.test.entity.State;
import com.clickbus.restapi.test.repository.StateRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StateServiceTest {

	@MockBean
	private StateRepository repository;

	@Autowired
	private StateService service;


	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.repository.save(Mockito.any(State.class))).willReturn(new State());
	}
	
	@Test
	public void testSave() {
		State c = this.service.save(new State());

		assertNotNull(c);
	}

}
