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

import com.clickbus.restapi.test.dto.PlaceDTO;
import com.clickbus.restapi.test.entity.City;
import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.entity.Place;
import com.clickbus.restapi.test.entity.PlaceClientApplication;
import com.clickbus.restapi.test.service.CityService;
import com.clickbus.restapi.test.service.PlaceClientApplicationService;
import com.clickbus.restapi.test.service.PlaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PlaceControlTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PlaceService service;
	@MockBean
	private CityService cityService;
	@MockBean
	private PlaceClientApplicationService placeService;

	private static final String URL_BASE = "/api/place/";
	private static final Long ID = 1L;
	private static final Long CITYID = 1L;
	private static final String NAME = "test";
	private static final String TERMINALNAME = "Terminal";
	private static final String SLUG = "Slug";
	private static final String ADRESS = "Adress";
	private static final String STATENAME = "State Name";
	private static final String CITYNAME = "City Name";
	private static final String COUNTRYNAME = "Country Name";
	
	@Test
	public void testSavePlace() throws Exception {
		Place place = this.getPlaceData();
		BDDMockito.given(this.service.save(Mockito.any(Place.class))).willReturn(place);
		BDDMockito.given(this.cityService.findById(Mockito.anyLong())).willReturn(Optional.of(new City()));

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.getJsonPost())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.name").value(NAME))
				.andExpect(jsonPath("$.data.adress").value(ADRESS))
				.andExpect(jsonPath("$.data.slug").value(SLUG))
				.andExpect(jsonPath("$.data.terminalName").value(TERMINALNAME))
				.andExpect(jsonPath("$.data.cityId").value(CITYID))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testGetPlaceById() throws Exception {
		Place p = this.getPlaceData();
		BDDMockito.given(this.service.findById(Mockito.anyLong())).willReturn(Optional.of(p));
		
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "?id=" + ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id").value(ID))
				.andExpect(jsonPath("$.data[0].name").value(NAME))
				.andExpect(jsonPath("$.data[0].adress").value(ADRESS))
				.andExpect(jsonPath("$.data[0].slug").value(SLUG))
				.andExpect(jsonPath("$.data[0].terminalName").value(TERMINALNAME))
				.andExpect(jsonPath("$.data[0].cityId").value(CITYID))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	public void testGetPlaceBySlug() throws Exception {
		Place p = this.getPlaceData();
		List<Place> list = new ArrayList<>();
		list.add(p);
		BDDMockito.given(this.service.findBySlugLikeIgnoreCase(Mockito.anyString())).willReturn(list);
		
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "?slug=" + SLUG)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id").value(ID))
				.andExpect(jsonPath("$.data[0].name").value(NAME))
				.andExpect(jsonPath("$.data[0].adress").value(ADRESS))
				.andExpect(jsonPath("$.data[0].slug").value(SLUG))
				.andExpect(jsonPath("$.data[0].terminalName").value(TERMINALNAME))
				.andExpect(jsonPath("$.data[0].cityId").value(CITYID))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	public void testGetEmptyResult() throws Exception {		
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isEmpty())
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testGetAllPlaces() throws Exception {
		BDDMockito.given(this.service.findAllPlaces()).willReturn(this.getObjectList());
		BDDMockito.given(this.placeService.findByPlaceId(Mockito.anyLong())).willReturn(this.getCities());
		List<String> list = new ArrayList<>();
		list.add(ID.toString());
		
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "all")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id").value(ID))
				.andExpect(jsonPath("$.data[0].name").value(NAME))
				.andExpect(jsonPath("$.data[0].slug").value(SLUG))
				.andExpect(jsonPath("$.data[0].city").value(CITYNAME))
				.andExpect(jsonPath("$.data[0].state").value(STATENAME))
				.andExpect(jsonPath("$.data[0].country").value(COUNTRYNAME))
				.andExpect(jsonPath("$.data[0].clientIds").value(list))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	private String getJsonPost() throws JsonProcessingException {
		PlaceDTO dto = new PlaceDTO();
		dto.setId(ID);
		dto.setName(NAME);
		dto.setAdress(ADRESS);
		dto.setSlug(SLUG);
		dto.setTerminalName(TERMINALNAME);
		dto.setCityId(CITYID);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

	private Place getPlaceData() {
		Place c = new Place();
		c.setId(ID);
		c.setName(NAME);
		c.setAdress(ADRESS);
		c.setSlug(SLUG);
		c.setTerminalName(TERMINALNAME);
		c.setCityId(new City());
		c.getCityId().setId(CITYID);
		return c;
	}
	
	private List<Object[]> getObjectList() {
		List<Object[]> list = new ArrayList<>();
		Object[] o = new Object[6];
		o[0] = ID;
		o[1] = NAME;
		o[2] = SLUG;
		o[3] = CITYNAME;
		o[4] = STATENAME;
		o[5] = COUNTRYNAME;
		
		list.add(o);
		return list;
	}
	
	private List<PlaceClientApplication> getCities() {
		List<PlaceClientApplication> l = new ArrayList<>();
		PlaceClientApplication p = new PlaceClientApplication();
		p.setClientId(new ClientApplication());
		p.getClientId().setId(ID);
		
		l.add(p);
		return l;
	}

}
