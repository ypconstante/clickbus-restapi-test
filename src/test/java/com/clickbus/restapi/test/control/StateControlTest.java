package com.clickbus.restapi.test.control;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.clickbus.restapi.test.dto.StateDTO;
import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.entity.State;
import com.clickbus.restapi.test.service.CountryService;
import com.clickbus.restapi.test.service.StateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StateControlTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StateService service;
	
	@MockBean
	private CountryService countryService;

	private static final String URL_BASE = "/api/state/";
	private static final Long ID = 1L;
	private static final String NAME = "TESTE";
	private static final Long COUNTRYID = 1L;

	@Test
	public void testSaveState() throws Exception {
		State state = this.getStateData();
		BDDMockito.given(this.service.save(Mockito.any(State.class))).willReturn(state);
		BDDMockito.given(this.countryService.findById(Mockito.anyLong())).willReturn(Optional.of(new Country()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.getJsonPost(null, NAME, COUNTRYID))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.name").value(NAME))
				.andExpect(jsonPath("$.data.countryId").value(COUNTRYID))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testSaveStateWithNullName() throws Exception {
		BDDMockito.given(this.countryService.findById(Mockito.anyLong())).willReturn(Optional.of(new Country()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, null, COUNTRYID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Name cannot be null"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	public void testSaveMaxSizeName() throws Exception {
		BDDMockito.given(this.countryService.findById(Mockito.anyLong())).willReturn(Optional.of(new Country()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, RandomStringUtils.random(101), COUNTRYID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3 and max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveMinSizeName() throws Exception {
		BDDMockito.given(this.countryService.findById(Mockito.anyLong())).willReturn(Optional.of(new Country()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, "", COUNTRYID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3 and max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveInvalidCountry() throws Exception {
		BDDMockito.given(this.countryService.findById(Mockito.anyLong())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, NAME, COUNTRYID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Country "+COUNTRYID+" does not exist"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	private String getJsonPost(Long id, String name, Long country) throws JsonProcessingException {
		StateDTO dto = new StateDTO();
		dto.setId(id);
		dto.setName(name);
		dto.setCountryId(country);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

	private State getStateData() {
		State c = new State();
		c.setId(ID);
		c.setName(NAME);
		c.setCountryId(new Country());
		c.getCountryId().setId(COUNTRYID);
		return c;
	}

}
