package com.romanpulov.violetnotewss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppInfoControllerTest extends BaseControllerMockMvcTest {

    public AppInfoControllerTest(WebApplicationContext context) {
        super(context);
    }

    @Test
    void testVersionInfo() throws Exception {
        runLogged(() -> {
            addResult(this.mvc.perform(MockMvcRequestBuilders.get("/app-info")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.version").exists())
                    .andReturn());

        }, "appInfo.log");
    }
}
