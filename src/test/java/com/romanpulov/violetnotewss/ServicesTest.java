package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.*;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.model.PassDataInfo;
import com.romanpulov.violetnotewss.services.PassDataFileManagementService;
import com.romanpulov.violetnotewss.services.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.services.PassDataFileReadException;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * Test for services
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PassDataManagementService.class, PassDataFileManagementService.class, MockServletContext.class})
public class ServicesTest {
    private static final String PASS_DATA_FILE_NAME = "data/test1.vnf";
    private static final String PASS_DATA_PASSWORD = "123456";
    private static final String PASS_DATA_WRONG_PASSWORD = "wrong_password";
    private static final String PASS_DATA_WRONG_FILE_NAME = "wrong_file_name";

    private File passDataFile;

    @Before
    public void init() {
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

    @Test(expected = PassDataFileNotFoundException.class)
    public void readPassDataNoFileName() throws Exception {
        passDataManagementService.readPassData(null, null);
    }

    @Test(expected = PassDataFileNotFoundException.class)
    public void readPassDataFileNotFound() throws Exception {
        passDataManagementService.readPassData(null, PASS_DATA_WRONG_FILE_NAME);
    }

    @Test(expected = PassDataFileReadException.class)
    public void readPassDataNoPassword() throws Exception {
        passDataManagementService.readPassData(null, passDataFile.getAbsolutePath());
    }

    @Test(expected = PassDataFileReadException.class)
    public void readPassDataEmptyPassword() throws Exception {
        passDataManagementService.readPassData(new PassDataInfo(""), passDataFile.getAbsolutePath());
    }

    @Test(expected = PassDataFileReadException.class)
    public void readPassDataFileReadWrongPassword() throws Exception {
        passDataManagementService.readPassData(
                new PassDataInfo(PASS_DATA_WRONG_PASSWORD),
                passDataFile.getAbsolutePath()
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
