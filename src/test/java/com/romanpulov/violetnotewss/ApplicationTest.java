package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.romanpulov.violetnotewss.application.Application;
import com.romanpulov.violetnotewss.controller.PassDataController;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes={Application.class})
//@TestPropertySource(properties = "server.servlet.context-parameters.fileName=\"Test property source file name\"")
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private TestRestTemplate restTemplate;

    @Autowired
    private ServletContext context;

    @Autowired
    private PassDataManagementService passDataManagementService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(context).isNotNull();
    }

    public void serviceLoads() throws Exception {
        assertThat(passDataManagementService).isNotNull();
    }

    @Test
    public void restHello() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("Hello from HelloController");
        //this.restTemplate.getForObject("http://localhost:" + port + "/dataitem", String.class)
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/dataitem", String.class)).isEqualTo("{\"my property id\":10}");
    }

    @Test
    public void restPassDataCheckPassword() {
        //System.out.println(this.restTemplate.postForObject("http://localhost:" + port + "/checkpasswordpost", "{password=\"pass1\"}", String.class));

        String url = "http://localhost:" + port + "/checkpasswordpost";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode passwordNode = mapper.createObjectNode();
        passwordNode.put("password", "pass123");

        System.out.println(passwordNode.toString());

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(passwordNode.toString(), headers);
        String answer = restTemplate.postForObject(url, entity, String.class);

        assertThat(answer).isEqualTo("pass123");
    }

    @Test
    public void fileName() {
        assertThat(context.getInitParameter(PassDataManagementService.PASS_DATA_FILE_NAME_PARAM_NAME)).isEqualTo("data/test1.vnf");
        String fileNameResult = this.restTemplate.getForObject("http://localhost:" + port + "/filename", String.class);
        assertThat(fileNameResult).contains("file exists:true");
    }
}
