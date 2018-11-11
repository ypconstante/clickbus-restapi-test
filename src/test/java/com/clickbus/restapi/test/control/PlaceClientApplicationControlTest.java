package com.clickbus.restapi.test.control;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.clickbus.restapi.test.dto.PlaceClientApplicationDTO;
import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.entity.PlaceClientApplication;
import com.clickbus.restapi.test.service.ClientApplicationService;
import com.clickbus.restapi.test.service.PlaceClientApplicationService;
import com.clickbus.restapi.test.service.PlaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PlaceClientApplicationControlTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PlaceClientApplicationService service;
	@MockBean
	private PlaceService placeService;
	@MockBean
	private ClientApplicationService clientService;
	
	private static final String URL_BASE = "/api/place-client-application/";
	private static final Long ID = 1L;
	private static final Long PLACEID = 1L;
	private static final Long CLIENTID = 1L;

	@Test
	public void testSavePlaceClientApplication() throws Exception {
		PlaceClientApplication clientApplication = this.getPlaceClientApplicationData();
		BDDMockito.given(this.service.save(Mockito.any(PlaceClientApplication.class))).willReturn(clientApplication);
		BDDMockito.given(this.placeService.findById(Mockito.anyLong())).willReturn(Optional.of(new Place()));
		BDDMockito.given(this.clientService.findById(Mockito.anyLong())).willReturn(Optional.of(new ClientApplication()));

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.getJsonPost(null, PLACEID, CLIENTID))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.placeId").value(PLACEID))
				.andExpect(jsonPath("$.data.clientId").value(CLIENTID))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testSavePlaceClientApplicationWithNullPlaceId() throws Exception {
		BDDMockito.given(this.clientService.findById(Mockito.anyLong())).willReturn(Optional.of(new ClientApplication()));
		List<String> l = new ArrayList<>();
		l.add("Place cannot be null");
		l.add("Place null does not exist");
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, CLIENTID, null))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value(l))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSavePlaceClientApplicationWithNullClientId() throws Exception {
		BDDMockito.given(this.placeService.findById(Mockito.anyLong())).willReturn(Optional.of(new Place()));
		List<String> l = new ArrayList<>();
		l.add("Client cannot be null");
		l.add("Client null does not exist");
		
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, null, PLACEID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value(l))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	private String getJsonPost(Long id, Long clientId, Long placeId) throws JsonProcessingException {
		PlaceClientApplicationDTO dto = new PlaceClientApplicationDTO();
		dto.setId(id);
		dto.setClientId(clientId);
		dto.setPlaceId(placeId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

	private PlaceClientApplication getPlaceClientApplicationData() {
		PlaceClientApplication c = new PlaceClientApplication();
		c.setId(ID);
		c.setClientId(new ClientApplication());
		c.getClientId().setId(CLIENTID);
		c.setPlaceId(new Place());
		c.getPlaceId().setId(PLACEID);
		return c;
	}

}
