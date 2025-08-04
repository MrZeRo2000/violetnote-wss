package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotewss.model.*;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassData2ControllerV2Test extends BaseControllerMockMvcTest {

    private static final String DATA_FILE_NAME = "data/test1.vnf";
    private static final String DATA_2_FILE_NAME = "data/test2.vnf";
    public static final String DATA_FILE_PASSWORD = "123456";

    public PassData2ControllerV2Test(WebApplicationContext context) {
        super(context);
    }

    @Test
    void testGetPassData2() throws Exception {
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
    void testSavePassData2() throws Exception {
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

            passData2DTO = generateTestPassData2();

            PassData2PersistRequest pr = new PassData2PersistRequest(testFileName, DATA_FILE_PASSWORD, passData2DTO);

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/edit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(pr))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].categoryName")
                            .value(passData2DTO.passCategoryList.get(0).categoryName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].system")
                            .value(passData2DTO.passCategoryList.get(0).passNote2List.get(0).system))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].user")
                            .value(passData2DTO.passCategoryList.get(0).passNote2List.get(0).user))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].password")
                            .value(passData2DTO.passCategoryList.get(0).passNote2List.get(0).password))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].url")
                            .value(passData2DTO.passCategoryList.get(0).passNote2List.get(0).url))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].info")
                            .value(passData2DTO.passCategoryList.get(0).passNote2List.get(0).info))
                    .andReturn()
            );

        }, "PassData2ControllerV2SavePassData.log");

        Path testPath = Paths.get(testFileFolder);

        try(var fl = Files.list(testPath)) {
            assertThat(fl.count()).isEqualTo(2);
        }

        try(var fl = Files.list(testPath)) {
            assertThat(fl.filter(path -> path.toString().endsWith("bak01")).count()).isEqualTo(1);
        }
    }

    @Test
    void testNewPassData2() throws Exception {
        String testFilePath = "test_new_pass_data_2";
        String testFileFolder = prepareTempDirFolder(testFilePath);
        String testFileName = testFileFolder + "/test_file_2.vnf";
        String testNewFileName = testFileFolder + "/test_file_2_new.vnf";
        Files.copy(Paths.get(DATA_2_FILE_NAME), Paths.get(testFileName));

        runLogged(() -> {
            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassData2PersistRequest(testFileName, DATA_FILE_PASSWORD, null)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode")
                            .value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                            .value(Matchers.startsWith("Error writing new file: the file already exist")))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassData2PersistRequest(testNewFileName, DATA_FILE_PASSWORD, null)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode")
                            .doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                            .doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].categoryName")
                            .value("New Category"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].system")
                            .value("New System"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].user")
                            .value("New User"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].password")
                            .value("New Password"))
                    .andReturn()
            );

        }, "PassData2ControllerV2NewPassData.log");

    }

    @Test
    void testFileInfo2() throws Exception {
        runLogged(()-> {

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/fileinfo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataFileRequest(DATA_2_FILE_NAME)))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.exists").value(true))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/fileinfo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataFileRequest(DATA_2_FILE_NAME + "6tfd")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.exists").value(false))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/fileinfo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataFileRequest("5: 6tfd")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode")
                            .value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andReturn()
            );

            addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/fileinfo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(mapper.writeValueAsString(new PassDataFileRequest("F:\\444 ")))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode")
                            .value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andReturn()
            );


        }, "PassData2ControllerV2GetFileInfo.log");
    }

}
