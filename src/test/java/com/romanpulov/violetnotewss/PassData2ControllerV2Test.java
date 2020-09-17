package com.romanpulov.violetnotewss;

import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassData2ControllerV2Test extends BaseControllerMockMvcTest {

    private static final String DATA_FILE_NAME = "data/test1.vnf";
    private static final String DATA_2_FILE_NAME = "data/test2.vnf";
    public static final String DATA_FILE_PASSWORD = "123456";

    public PassData2ControllerV2Test(WebApplicationContext context) {
        super(context);
    }

    @Test
    void testGetPassData() throws Exception {
        runLogged(() -> {
            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString((new PassDataGetRequest("non_existing_file", "dummy"))))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Matchers.startsWith("File not found")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest("", "dummy")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Matchers.startsWith("File not found")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest(DATA_FILE_NAME + "1xq3", "dummy")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Matchers.startsWith("File not found")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest(DATA_FILE_NAME, DATA_FILE_PASSWORD)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList", Matchers.hasSize(4)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList", Matchers.hasSize(3)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[1].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[1].noteList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[2].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[2].noteList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[3].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[3].noteList", Matchers.hasSize(2)))
                    .andReturn()
            );

        }, "PassData2ControllerV2GetPassData.log");
    }
}