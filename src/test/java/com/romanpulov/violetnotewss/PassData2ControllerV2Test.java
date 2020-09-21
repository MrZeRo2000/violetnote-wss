package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotewss.model.PassCategory2DTO;
import com.romanpulov.violetnotewss.model.PassData2DTO;
import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import com.romanpulov.violetnotewss.model.PassNote2DTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private PassData2DTO generateTestPassData2() {
        PassCategory2DTO passCategory2DTO = new PassCategory2DTO("New Category", new ArrayList<>());
        PassCategory2DTO passCategory2EmptyDTO = new PassCategory2DTO("Empty Category", new ArrayList<>());

        PassNote2DTO passNote2DTO = new PassNote2DTO("system", "user",
                "password", "url", "info", null, null, true);
        passCategory2DTO.passNote2List.add(passNote2DTO);

        List<PassCategory2DTO> passCategory2DTOList = Arrays.asList(passCategory2DTO, passCategory2EmptyDTO);

        return new PassData2DTO(passCategory2DTOList);
    }

    @Test
    void testSavePassData() throws Exception {
        String testFilePath = "test_save_pass_data_2";
        String testFileFolder = prepareTempDirFolder(testFilePath);
        String testFileName = testFileFolder + "/test_file_2.vnf";
        Files.copy(Paths.get(DATA_2_FILE_NAME), Paths.get(testFileName));

        runLogged(() -> {
            // read initial data
            MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest(testFileName, DATA_FILE_PASSWORD)))
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

                    .andReturn();

            addResult(result);

            ObjectMapper mapper = new ObjectMapper();
            PassData2DTO passData2DTO = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    PassData2DTO.class
            );

            Assertions.assertEquals(4, passData2DTO.passCategoryList.size());
            Assertions.assertEquals(7, passData2DTO.passCategoryList.stream().mapToInt(value -> value.passNote2List.size()).sum());

        }, "PassData2ControllerV2SavePassData.log");
    }

}
