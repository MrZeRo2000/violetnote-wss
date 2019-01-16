package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.*;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.model.PassDataInfo;
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

import javax.servlet.ServletContext;
import java.io.File;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PassDataManagementService.class, MockServletContext.class})
public class ServicesTest {
    private static final String PASS_DATA_FILE_NAME = "data/test1.vnf";

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

    @Test
    public void serviceLoads() throws Exception {
        assertThat(passDataManagementService).isNotNull();
    }

    @Test(expected = PassDataFileNotFoundException.class)
    public void readPassDataNoFileName() throws Exception {
        passDataManagementService.readPassData(null, null);
    }

    @Test(expected = PassDataFileNotFoundException.class)
    public void readPassDataFileNotFound() throws Exception {
        passDataManagementService.readPassData(null, "any_non_existing_file");
    }

    @Test(expected = PassDataFileReadException.class)
    public void readPassDataNoPassword() throws Exception {
        passDataManagementService.readPassData(null, passDataFile.getAbsolutePath());
    }

    @Test(expected = PassDataFileReadException.class)
    public void readPassDataEmptyPassword() throws Exception {
        passDataManagementService.readPassData(PassDataInfo.fromString(""), passDataFile.getAbsolutePath());
    }

    @Test(expected = PassDataFileReadException.class)
    public void readPassDataFileReadWrongPassword() throws Exception {
        passDataManagementService.readPassData(
                PassDataInfo.fromString("wrong_password"),
                passDataFile.getAbsolutePath()
        );
    }

    @Test
    public void readPassData() throws Exception {
        PassData passData = passDataManagementService.readPassData(
                PassDataInfo.fromString("123456"),
                passDataFile.getAbsolutePath()
        );

        assertThat(passData).isNotNull();
        assertThat(passData.getPassCategoryList().size()).isEqualTo(4);
        assertThat(passData.getPassNoteList().size()).isEqualTo(7);
    }

}
