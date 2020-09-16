package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.*;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotewss.model.PassDataInfo;
import com.romanpulov.violetnotewss.services.PassData2ManagementService;
import com.romanpulov.violetnotewss.services.PassDataFileManagementService;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Test for services
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class ServicesTest {
    private static final String PASS_DATA_FILE_NAME = "data/test1.vnf";
    private static final String PASS_DATA_2_FILE_NAME = "data/test2.vnf";
    private static final String PASS_DATA_PASSWORD = "123456";
    private static final String PASS_DATA_WRONG_PASSWORD = "wrong_password";
    private static final String PASS_DATA_WRONG_FILE_NAME = "wrong_file_name";

    private static File passDataFile;
    private static File passData2File;

    @BeforeAll
    static void init() {
        passDataFile = new File(PASS_DATA_FILE_NAME);
        if (!passDataFile.exists()) {
            throw new RuntimeException("File name:" + passDataFile.getAbsoluteFile() + " not found");
        }
        passData2File = new File(PASS_DATA_2_FILE_NAME);
        if (!passData2File.exists()) {
            throw new RuntimeException("File name:" + passData2File.getAbsoluteFile() + " not found");
        }
    }

    @Autowired
    private PassDataManagementService passDataManagementService;

    @Autowired
    private PassData2ManagementService passData2ManagementService;


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
        Assertions.assertThrows(
                PassDataFileNotFoundException.class,
                () -> passData2ManagementService.readPassData(null, null)
        );
    }

    @Test
    public void readPassDataFileNotFound() {
        Assertions.assertThrows(
                PassDataFileNotFoundException.class,
                () -> passDataManagementService.readPassData(null, PASS_DATA_WRONG_FILE_NAME)
        );
        Assertions.assertThrows(
                PassDataFileNotFoundException.class,
                () -> passData2ManagementService.readPassData(null, PASS_DATA_WRONG_FILE_NAME)
        );
    }

    @Test
    public void readPassDataNoPassword() {
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passDataManagementService.readPassData(null, passDataFile.getAbsolutePath())
        );
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passData2ManagementService.readPassData(null, passData2File.getAbsolutePath())
        );
    }

    @Test
    public void readPassDataEmptyPassword() {
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passDataManagementService.readPassData(new PassDataInfo(""), passDataFile.getAbsolutePath())
        );
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passData2ManagementService.readPassData(new PassDataInfo(""), passData2File.getAbsolutePath())
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
        Assertions.assertThrows(
                PassDataFileReadException.class,
                () -> passData2ManagementService.readPassData(
                        new PassDataInfo(PASS_DATA_WRONG_PASSWORD),
                        passData2File.getAbsolutePath()
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

        PassData2 passData21 = passData2ManagementService.readPassData(
                new PassDataInfo(PASS_DATA_PASSWORD),
                passDataFile.getAbsolutePath()
        );

        assertThat(passData21).isNotNull();
        assertThat(passData21.getCategoryList().size()).isEqualTo(4);
        assertThat(passData21.getCategoryList().stream().mapToInt(value -> value.getNoteList().size()).sum()).isEqualTo(7);

        PassData2 passData22 = passData2ManagementService.readPassData(
                new PassDataInfo(PASS_DATA_PASSWORD),
                passData2File.getAbsolutePath()
        );

        assertThat(passData22).isNotNull();
        assertThat(passData22.getCategoryList().size()).isEqualTo(4);
        assertThat(passData22.getCategoryList().stream().mapToInt(value -> value.getNoteList().size()).sum()).isEqualTo(7);
    }
}
