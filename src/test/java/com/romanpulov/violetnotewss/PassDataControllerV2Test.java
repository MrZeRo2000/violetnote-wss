package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotewss.model.*;
import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassDataControllerV2Test extends BaseControllerMockMvcTest {

    private static final String DATA_FILE_NAME = "data/test1.vnf";
    public static final String DATA_FILE_PASSWORD = "123456";

    public PassDataControllerV2Test(WebApplicationContext context) {
        super(context);
    }

    @Test
    void testGetPassData() throws Exception {
        runLogged(() -> {
            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString((new PassDataGetRequest("non_existing_file", "dummy"))))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Matchers.startsWith("File not found")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest("", "dummy")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Matchers.startsWith("File not found")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest(DATA_FILE_NAME + "1xq3", "dummy")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Matchers.startsWith("File not found")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest(DATA_FILE_NAME, DATA_FILE_PASSWORD)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList", Matchers.hasSize(4)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList", Matchers.hasSize(7)))
                    .andReturn()
            );

        }, "PassDataControllerV2GetPassData.log");
    }

    private PassDataDTO getTestPassData() {
        PassCategoryDTO passCategoryDTO = new PassCategoryDTO("New Category", null);
        PassCategoryDTO passCategoryEmptyDTO = new PassCategoryDTO("Empty Category", null);
        PassNoteDTO passNoteDTO = new PassNoteDTO(passCategoryDTO, "system", "user",
                "password", "comments", "custom", "info", null, null, true);

        List<PassCategoryDTO> passCategoryDTOList = new ArrayList<>();
        passCategoryDTOList.add(passCategoryDTO);
        passCategoryDTOList.add(passCategoryEmptyDTO);

        List<PassNoteDTO> passNoteDTOList = new ArrayList<>();
        passNoteDTOList.add(passNoteDTO);

        return new PassDataDTO(passCategoryDTOList, passNoteDTOList);
    }

    @Test
    void testSavePassData() throws Exception {
        String testFilePath = "test_save_pass_data";
        String testFileFolder = prepareTempDirFolder(testFilePath);
        String testFileName = testFileFolder + "/test_file.vnf";
        Files.copy(Paths.get(DATA_FILE_NAME), Paths.get(testFileName));

        runLogged(() -> {
            // read initial data
            MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataGetRequest(testFileName, DATA_FILE_PASSWORD)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList", Matchers.hasSize(4)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList", Matchers.hasSize(7)))
                    .andReturn();

            addResult(result);

            ObjectMapper mapper = new ObjectMapper();
            PassDataDTO passData = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    PassDataDTO.class
            );

            Assertions.assertEquals(4, passData.passCategoryList.size());
            Assertions.assertEquals(7, passData.passNoteList.size());

            PassDataDTO passDataDTO = getTestPassData();

            PassDataPersistRequest pr = new PassDataPersistRequest(testFileName, DATA_FILE_PASSWORD, passDataDTO);

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata/edit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(pr))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList[0].categoryName")
                            .value(passDataDTO.passCategoryList.get(0).categoryName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList[0].parentCategory").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].passCategory.categoryName")
                            .value(passDataDTO.passCategoryList.get(0).categoryName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].system")
                            .value(passDataDTO.passNoteList.get(0).system))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].user")
                            .value(passDataDTO.passNoteList.get(0).user))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].password")
                            .value(passDataDTO.passNoteList.get(0).password))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].comments")
                            .value(passDataDTO.passNoteList.get(0).comments))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].custom")
                            .value(passDataDTO.passNoteList.get(0).custom))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].info")
                            .value(passDataDTO.passNoteList.get(0).info))
                    .andReturn()
            );

        }, "PassDataControllerV2SavePassData.log");

        Assertions.assertEquals(2, Files.list(Paths.get(testFileFolder)).count());
        Assertions.assertEquals(1,
                Files.list(Paths.get(testFileFolder)).filter(path -> path.toString().endsWith("bak01")).count());

    }

    @Test
    void testNewPassData() throws Exception {
        String testFilePath = "test_new_pass_data";
        String testFileFolder = prepareTempDirFolder(testFilePath);
        String testFileName = testFileFolder + "/test_file.vnf";
        String testNewFileName = testFileFolder + "/test_file_new.vnf";
        Files.copy(Paths.get(DATA_FILE_NAME), Paths.get(testFileName));

        PassDataDTO passDataDTO = getTestPassData();

        runLogged(() -> {

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataPersistRequest(testFileName, DATA_FILE_PASSWORD, passDataDTO)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode")
                            .value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                            .value(Matchers.startsWith("Error writing new file: the file already exist")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataPersistRequest(testNewFileName, DATA_FILE_PASSWORD, passDataDTO)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode")
                            .doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                            .doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList[0].categoryName")
                            .value(passDataDTO.passCategoryList.get(0).categoryName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passCategoryList[0].parentCategory").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].passCategory.categoryName")
                            .value(passDataDTO.passCategoryList.get(0).categoryName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].system")
                            .value(passDataDTO.passNoteList.get(0).system))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].user")
                            .value(passDataDTO.passNoteList.get(0).user))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].password")
                            .value(passDataDTO.passNoteList.get(0).password))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].comments")
                            .value(passDataDTO.passNoteList.get(0).comments))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].custom")
                            .value(passDataDTO.passNoteList.get(0).custom))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.passNoteList[0].info")
                            .value(passDataDTO.passNoteList.get(0).info))
                    .andReturn()
            );

        }, "PassDataControllerV2NewPassData.log");
    }
}
