package com.clickbus.restapi.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public abstract class AppControllerTestBootstrapper extends AppTestBootstrapper {
    @Autowired private MockMvc mockMvc;

    protected ResultActions doGet(String urlTemplate, Object... uriVars) throws Exception {
        return this.mockMvc
            .perform(MockMvcRequestBuilders.get(urlTemplate, uriVars))
            .andDo(print());
    }

}
