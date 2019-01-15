package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.assertThat;

import com.romanpulov.violetnotewss.application.Application;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PassDataManagementService.class, ServicesTest.TestConfig.class})
public class ServicesTest {

    @Autowired
    private PassDataManagementService passDataManagementService;

    @Configuration
    public static class TestConfig {
        @Bean
        public ServletContext getServletContext() {
            return new MockServletContext();
        }
    }

    @Test
    public void serviceLoads() throws Exception {
        assertThat(passDataManagementService).isNotNull();
    }

}
