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

import com.clickbus.restapi.test.dto.CityDTO;
import com.clickbus.restapi.test.entity.State;
import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.service.StateService;
import com.clickbus.restapi.test.service.CityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CityControlTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CityService service;
	
	@MockBean
	private StateService stateService;

	private static final String URL_BASE = "/api/city/";
	private static final Long ID = 1L;
	private static final String NAME = "TESTE";
	private static final Long STATEID = 1L;

	@Test
	public void testSaveCity() throws Exception {
		City city = this.getCityData();
		BDDMockito.given(this.service.save(Mockito.any(City.class))).willReturn(city);
		BDDMockito.given(this.stateService.findById(Mockito.anyLong())).willReturn(Optional.of(new State()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.getJsonPost(null, NAME, STATEID))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.name").value(NAME))
				.andExpect(jsonPath("$.data.stateId").value(STATEID))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testSaveCityWithNullName() throws Exception {
		BDDMockito.given(this.stateService.findById(Mockito.anyLong())).willReturn(Optional.of(new State()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, null, STATEID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Name cannot be null"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	public void testSaveMaxSizeName() throws Exception {
		BDDMockito.given(this.stateService.findById(Mockito.anyLong())).willReturn(Optional.of(new State()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, RandomStringUtils.random(101), STATEID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3 and max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveMinSizeName() throws Exception {
		BDDMockito.given(this.stateService.findById(Mockito.anyLong())).willReturn(Optional.of(new State()));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, "", STATEID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3 and max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveInvalidState() throws Exception {
		BDDMockito.given(this.stateService.findById(Mockito.anyLong())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, NAME, STATEID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("State "+STATEID+" does not exist"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	private String getJsonPost(Long id, String name, Long state) throws JsonProcessingException {
		CityDTO dto = new CityDTO();
		dto.setId(id);
		dto.setName(name);
		dto.setStateId(state);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

	private City getCityData() {
		City c = new City();
		c.setId(ID);
		c.setName(NAME);
		c.setStateId(new State());
		c.getStateId().setId(STATEID);
		return c;
	}

}
