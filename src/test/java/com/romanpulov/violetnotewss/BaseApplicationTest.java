package com.romanpulov.violetnotewss;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Base class for test which require application context
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected ServletContext context;

    @Autowired
    protected TestRestTemplate restTemplate;

    String getBaseUrl() {
        return "http://localhost:" + port + "/";
    }
}
