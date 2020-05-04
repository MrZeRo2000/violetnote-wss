package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.*;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.application.Application;
import com.romanpulov.violetnotewss.model.PassDataInfo;
import com.romanpulov.violetnotewss.services.PassDataFileManagementService;
import com.romanpulov.violetnotewss.services.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.services.PassDataFileReadException;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Test for services
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes={Application.class})
public class ServicesTest {
    private static final String PASS_DATA_FILE_NAME = "data/test1.vnf";
    private static final String PASS_DATA_PASSWORD = "123456";
    private static final String PASS_DATA_WRONG_PASSWORD = "wrong_password";
    private static final String PASS_DATA_WRONG_FILE_NAME = "wrong_file_name";

    private static File passDataFile;

    @BeforeAll
    static void init() {
        passDataFile = new File(PASS_DATA_FILE_NAME);
        if (!passDataFile.exists()) {
            throw new RuntimeException("File name:" + passDataFile.getAbsoluteFile() + " not found");
        }
    }

    @Autowired
    private PassDataManagementService passDataManagementService;

    @Autowired
    private PassDataFileManagementService passDataFileManagementService;

    @Test
    public void serviceLoads() {
        assertThat(passDataManagementService).isNotNull();
    }

    @Test
    public void readPassDataNoFileName() {
        Assertions.assertThrows(
                PassDataFileNotFoundException.class,
                () -> passDataManagementService.readPassData(null, null)
        );
    }

    @Test
    public void readPassDataFileNotFound() {
        Assertions.assertThrows(
                PassDataFileNotFoundException.class,
                () -> passDataManagementService.readPassData(null, PASS_DATA_WRONG_FILE_NAME)
        );
    }

    @Test
    public void readPassDataNoPassword() {
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passDataManagementService.readPassData(null, passDataFile.getAbsolutePath())
        );
    }

    @Test
    public void readPassDataEmptyPassword() {
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passDataManagementService.readPassData(new PassDataInfo(""), passDataFile.getAbsolutePath())
        );
    }

    @Test
    public void readPassDataFileReadWrongPassword() {
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passDataManagementService.readPassData(
                        new PassDataInfo(PASS_DATA_WRONG_PASSWORD),
                        passDataFile.getAbsolutePath()
                )
        );
    }

    @Test
    public void readPassData() throws Exception {
        PassData passData = passDataManagementService.readPassData(
                new PassDataInfo(PASS_DATA_PASSWORD),
                passDataFile.getAbsolutePath()
        );

        assertThat(passData).isNotNull();
        assertThat(passData.getPassCategoryList().size()).isEqualTo(4);
        assertThat(passData.getPassNoteList().size()).isEqualTo(7);
    }
}
