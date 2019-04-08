package com.clickbus.restapi.web.controller;

import com.clickbus.restapi.test.AppControllerTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SwaggerControllerTest extends AppControllerTestBootstrapper {
    @Autowired MockMvc mockMvc;

    @Test
    void ui() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.get("/swagger-ui.html"))
            .andExpect(status().isOk());
    }

    @Test
    void apiDocs() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.get("/v2/api-docs"))
            .andExpect(status().isOk());
    }
}
