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

import com.clickbus.restapi.test.dto.ClientApplicationDTO;
import com.clickbus.restapi.test.entity.ClientApplication;
import com.clickbus.restapi.test.service.ClientApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientApplicationControlTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ClientApplicationService service;

	private static final String URL_BASE = "/api/client-application/";
	private static final Long ID = 1L;
	private static final String NAME = "test";
	private static final String PUBLIC_NAME = "Pub name";

	@Test
	public void testSaveClientApplication() throws Exception {
		ClientApplication clientApplication = this.getClientApplicationData();
		BDDMockito.given(this.service.save(Mockito.any(ClientApplication.class))).willReturn(clientApplication);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.getJsonPost(null, NAME, PUBLIC_NAME))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.name").value(NAME))
				.andExpect(jsonPath("$.data.publicName").value(PUBLIC_NAME))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	public void testSaveClientApplicationWithNullName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, null, PUBLIC_NAME))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Name cannot be null"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	public void testSaveMaxSizeName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, RandomStringUtils.random(101), PUBLIC_NAME))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3 and max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveMinSizeName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, "", PUBLIC_NAME))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("The name min size is 3 and max size is 100"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	public void testSaveClientApplicationWithNullPublicName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, NAME, null))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Public name cannot be null"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	public void testSaveMaxSizePublicName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, NAME, RandomStringUtils.random(300)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Public name min size is 3 and max size is 255"))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSaveMinSizePublicName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonPost(null, NAME, ""))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Public name min size is 3 and max size is 255"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	private String getJsonPost(Long id, String name, String publicName) throws JsonProcessingException {
		ClientApplicationDTO dto = new ClientApplicationDTO();
		dto.setId(id);
		dto.setName(name);
		dto.setPublicName(publicName);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

	private ClientApplication getClientApplicationData() {
		ClientApplication c = new ClientApplication();
		c.setId(ID);
		c.setName(NAME);
		c.setPublicName(PUBLIC_NAME);
		return c;
	}

}
