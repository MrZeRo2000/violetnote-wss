package com.romanpulov.violetnotewss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.ServletContext;

/**
 * Base class for test which require application context
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes={Application.class})
public abstract class BaseApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected ServletContext context;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected TestRestTemplate restTemplate;

    String getBaseUrl() {
        return "http://localhost:" + port + "/";
    }
}
