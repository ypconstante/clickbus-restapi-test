package com.clickbus.restapi.test.control;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.clickbus.restapi.test.dto.CountryDTO;
import com.clickbus.restapi.test.entity.Country;
import com.clickbus.restapi.test.service.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CountryControlTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CountryService service;

	private static final String URL_BASE = "/api/country/";
	private static final Long ID = 1L;
	private static final String NAME = "test";

	@Test
	public void testSaveCountry() throws Exception {
		Country country = this.getCountryData();
		BDDMockito.given(this.service.save(Mockito.any(Country.class))).willReturn(country);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.getJsonPost(null, NAME))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.name").value(NAME))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testSaveCountryWithNullName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, null))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Name cannot be null"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	public void testSaveMaxSizeName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, RandomStringUtils.random(101)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveMinSizeName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, ""))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	private String getJsonPost(Long id, String name) throws JsonProcessingException {
		CountryDTO dto = new CountryDTO();
		dto.setId(id);
		dto.setName(name);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

	private Country getCountryData() {
		Country c = new Country();
		c.setId(ID);
		c.setName(NAME);
		return c;
	}

}
