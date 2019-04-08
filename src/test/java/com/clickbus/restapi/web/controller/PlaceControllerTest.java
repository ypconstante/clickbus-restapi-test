package com.clickbus.restapi.web.controller;

import com.clickbus.restapi.test.AppControllerTestBootstrapper;
import com.clickbus.restapi.web.dto.PlaceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlaceControllerTest extends AppControllerTestBootstrapper {
    @Autowired ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        MvcResult mvcResult = doGet("/places")
            .andExpect(status().isOk())
            .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        PlaceDto[] content = this.objectMapper.readValue(json, PlaceDto[].class);
        assertThat(content).hasSize(16);
        assertThat(content[0].getId()).isEqualTo(1L);
        assertThat(content[0].getState())
            .isEqualTo(new PlaceDto.State().setName("Santa Catarina"));
    }

    @Test
    void findAllWithSlug() throws Exception {
        MvcResult mvcResult = doGet("/places?slug=terminal-floripa-02")
            .andExpect(status().isOk())
            .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        PlaceDto[] content = this.objectMapper.readValue(json, PlaceDto[].class);
        assertThat(content).hasSize(1);
        assertThat(content[0].getId()).isEqualTo(2L);
    }

    @Test
    void findAllWithSlugNonexistent() throws Exception {
        doGet("/places?slug=xyz")
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void findById() throws Exception {
        MvcResult mvcResult = doGet("/places/1")
            .andExpect(status().isOk())
            .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        PlaceDto content = this.objectMapper.readValue(json, PlaceDto.class);
        assertThat(content.getId()).isEqualTo(1L);
        assertThat(content.getState())
            .isEqualTo(new PlaceDto.State().setName("Santa Catarina"));
    }

    @Test
    void findByIdNonexistent() throws Exception {
        doGet("/places/99999")
            .andExpect(status().isNotFound());
    }
}
