package com.romanpulov.violetnotewss;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.romanpulov.violetnotewss.application.Application;
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
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes={Application.class})
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private TestRestTemplate restTemplate;

    @Autowired
    private ServletContext context;

    @Test
    public void contextLoads() throws Exception {
        assertThat(context).isNotNull();
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
    public void initParameter() {
        System.out.println("FileName:" + context.getInitParameter("fileName"));
    }
}
