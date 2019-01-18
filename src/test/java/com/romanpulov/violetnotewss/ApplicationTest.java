package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.assertThat;

import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@TestPropertySource(properties = "server.servlet.context-parameters.fileName=\"Test property source file name\"")

/**
 * Common application test
 */
public class ApplicationTest extends BaseApplicationTest {

    @Autowired
    private PassDataManagementService passDataManagementService;

    @Value("${server.servlet.context-parameters.pass-data-file-name}")
    private String passDataFileName;

    @Test
    public void contextLoaded() {
        assertThat(context).isNotNull();
    }

    @Test
    public void restTemplateLoaded() {
        assertThat(restTemplate).isNotNull();
    }

    public void serviceLoads() {
        assertThat(passDataManagementService).isNotNull();
    }
}
