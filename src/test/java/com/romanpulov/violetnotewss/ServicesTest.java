package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.assertThat;

import com.romanpulov.violetnotewss.application.Application;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={PassDataManagementService.class})
//@SpringBootTest(classes={Application.class})
public class ServicesTest {

    @Autowired
    private PassDataManagementService passDataManagementService;

    @Test
    public void serviceLoads() throws Exception {
        assertThat(passDataManagementService).isNotNull();
    }

}
