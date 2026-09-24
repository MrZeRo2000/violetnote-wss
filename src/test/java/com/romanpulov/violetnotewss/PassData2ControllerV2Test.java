package com.romanpulov.violetnotewss;

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
import tools.jackson.databind.json.JsonMapper;

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

    private static final String DATA_FILE_NAME = TestConfiguration.TEST_ROOT_PATH.resolve("test1.vnf").toString();
    private static final String DATA_2_FILE_NAME = TestConfiguration.TEST_ROOT_PATH.resolve("test2.vnf").toString();
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
                    .andExpect(MockMvcResultMatchers.jsonPath("$.noteAttr").doesNotExist())
                    .andReturn()
            );

        }, "PassData2ControllerV2GetPassData.log");
    }

    private PassData2DTO generateTestPassData2() {
        PassCategory2DTO passCategory2DTO = new PassCategory2DTO("New Category", new ArrayList<>());
        PassCategory2DTO passCategory2EmptyDTO = new PassCategory2DTO("Empty Category", new ArrayList<>());

        PassNote2DTO passNote2DTO1 = new PassNote2DTO("system", "user",
                "password", "url", "info", null, null, true, null);
        passCategory2DTO.noteList().add(passNote2DTO1);

        PassNote2DTO passNote2DTO2 = new PassNote2DTO("system 2", "user 2",
                "password 2", "url2", "info2", null, null, true,
                List.of(
                        new PassDataAttributeDTO("name1", "value1"),
                        new PassDataAttributeDTO("name2", "value2")
                )
                );
        passCategory2DTO.noteList().add(passNote2DTO2);

        List<PassCategory2DTO> passCategory2DTOList = Arrays.asList(passCategory2DTO, passCategory2EmptyDTO);

        return new PassData2DTO(passCategory2DTOList);
    }

    private PassData2DTO generateTestPassData2Changed() {
        PassCategory2DTO passCategory2DTO = new PassCategory2DTO("Updated Category", new ArrayList<>());
        PassCategory2DTO passCategory2EmptyDTO = new PassCategory2DTO("Another Empty Category", new ArrayList<>());

        PassNote2DTO passNote2DTO1 = new PassNote2DTO("system changed", "user changed",
                "password changed", "url changed", "info changed", null, null, true,
                List.of(
                        new PassDataAttributeDTO("changedName1", "changedValue1")
                ));
        passCategory2DTO.noteList().add(passNote2DTO1);

        PassNote2DTO passNote2DTO2 = new PassNote2DTO("system 2 changed", "user 2 changed",
                "password 2 changed", "url2 changed", "info2 changed", null, null, true,
                List.of(
                        new PassDataAttributeDTO("name1 changed", "value1 changed"),
                        new PassDataAttributeDTO("name2 changed", "value2 changed"),
                        new PassDataAttributeDTO("name3 changed", "value3 changed")
                )
                );
        passCategory2DTO.noteList().add(passNote2DTO2);

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
                    .andExpect(MockMvcResultMatchers.jsonPath("$.noteAttr").doesNotExist())
                    .andReturn();

            addResult(result);

            JsonMapper mapper = new JsonMapper();
            PassData2DTO passData2DTO = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    PassData2DTO.class
            );

            Assertions.assertEquals(4, passData2DTO.categoryList().size());
            Assertions.assertEquals(7, passData2DTO.categoryList().stream().mapToInt(value -> value.noteList().size()).sum());

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
                            .value(passData2DTO.categoryList().getFirst().categoryName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].system")
                            .value(passData2DTO.categoryList().getFirst().noteList().getFirst().system()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].user")
                            .value(passData2DTO.categoryList().getFirst().noteList().getFirst().user()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].password")
                            .value(passData2DTO.categoryList().getFirst().noteList().getFirst().password()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].url")
                            .value(passData2DTO.categoryList().getFirst().noteList().getFirst().url()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryList[0].noteList[0].info")
                            .value(passData2DTO.categoryList().getFirst().noteList().getFirst().info()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.noteAttr").doesNotExist())
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

    /**
     * Creates a brand-new pass data file protected with {@link #DATA_FILE_PASSWORD} and saves
     * {@code passData2DTO} into it. Shared setup step for the attribute round-trip tests below.
     */
    private String createAndSavePassData2File(String testFilePath, PassData2DTO passData2DTO) throws Exception {
        String testFileFolder = prepareTempDirFolder(testFilePath);
        String testFileName = testFileFolder + "/test_file_2.vnf";

        addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/new")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(mapper.writeValueAsString(new PassData2PersistRequest(testFileName, DATA_FILE_PASSWORD, null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                .andReturn()
        );

        savePassData2(testFileName, passData2DTO);

        return testFileName;
    }

    private void savePassData2(String testFileName, PassData2DTO passData2DTO) throws Exception {
        addResult(this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(mapper.writeValueAsString(new PassData2PersistRequest(testFileName, DATA_FILE_PASSWORD, passData2DTO)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                .andReturn()
        );
    }

    private PassData2DTO getPassData2(String testFileName) throws Exception {
        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/v2/passdata2")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(mapper.writeValueAsString(new PassDataGetRequest(testFileName, DATA_FILE_PASSWORD)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").doesNotExist())
                .andReturn();

        addResult(result);

        return mapper.readValue(result.getResponse().getContentAsString(), PassData2DTO.class);
    }

    @Test
    void testSaveAndGetPassData2Attributes() throws Exception {
        PassData2DTO initialPassData2DTO = generateTestPassData2();

        runLogged(() -> {
            String testFileName = createAndSavePassData2File("test_save_get_pass_data_2_attributes", initialPassData2DTO);

            PassData2DTO retrievedPassData2DTO = getPassData2(testFileName);

            // full round-trip equality check, including the previously untested attributes field
            Assertions.assertEquals(initialPassData2DTO, retrievedPassData2DTO);
            Assertions.assertNull(retrievedPassData2DTO.categoryList().getFirst().noteList().get(0).attributes());
            Assertions.assertEquals(2, retrievedPassData2DTO.categoryList().getFirst().noteList().get(1).attributes().size());

        }, "PassData2ControllerV2SaveAndGetPassDataAttributes.log");
    }

    @Test
    void testChangeAndGetPassData2Attributes() throws Exception {
        PassData2DTO initialPassData2DTO = generateTestPassData2();
        PassData2DTO changedPassData2DTO = generateTestPassData2Changed();

        runLogged(() -> {
            String testFileName = createAndSavePassData2File("test_change_get_pass_data_2_attributes", initialPassData2DTO);

            // the data was changed in the meantime, save the new version with the same password
            savePassData2(testFileName, changedPassData2DTO);

            PassData2DTO retrievedPassData2DTO = getPassData2(testFileName);

            // full round-trip equality check of the changed data, confirming the update overwrote the initial attributes
            Assertions.assertEquals(changedPassData2DTO, retrievedPassData2DTO);
            Assertions.assertNotEquals(initialPassData2DTO, retrievedPassData2DTO);

        }, "PassData2ControllerV2ChangeAndGetPassDataAttributes.log");
    }

    @Test
    void testRemoveAndGetPassData2Attributes() throws Exception {
        PassData2DTO initialPassData2DTO = generateTestPassData2();

        runLogged(() -> {
            String testFileName = createAndSavePassData2File("test_remove_get_pass_data_2_attributes", initialPassData2DTO);

            // strip the attributes off the previously saved notes, reusing the same test data
            List<PassNote2DTO> noAttrNoteList = initialPassData2DTO.categoryList().getFirst().noteList().stream()
                    .map(note -> new PassNote2DTO(note.system(), note.user(), note.password(), note.url(), note.info(),
                            note.createdDate(), note.modifiedDate(), note.active(), null))
                    .toList();
            PassData2DTO noAttrPassData2DTO = new PassData2DTO(Arrays.asList(
                    new PassCategory2DTO(initialPassData2DTO.categoryList().getFirst().categoryName(), new ArrayList<>(noAttrNoteList)),
                    initialPassData2DTO.categoryList().get(1)
            ));

            savePassData2(testFileName, noAttrPassData2DTO);

            PassData2DTO retrievedPassData2DTO = getPassData2(testFileName);

            Assertions.assertEquals(noAttrPassData2DTO, retrievedPassData2DTO);
            retrievedPassData2DTO.categoryList().getFirst().noteList()
                    .forEach(note -> Assertions.assertNull(note.attributes()));

        }, "PassData2ControllerV2RemoveAndGetPassDataAttributes.log");
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
                    .andExpect(MockMvcResultMatchers.jsonPath("$.noteAttr").doesNotExist())
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
