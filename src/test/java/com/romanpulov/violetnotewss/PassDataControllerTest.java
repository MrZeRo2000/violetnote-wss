package com.romanpulov.violetnotewss;

import com.romanpulov.violetnotewss.model.ErrorResponse;
import com.romanpulov.violetnotewss.model.PassDataDTO;
import com.romanpulov.violetnotewss.services.PassDataFileManagementService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for PassDataController
 */
public class PassDataControllerTest extends BaseApplicationTest {

    @Override
    protected String getBaseUrl() {
        return super.getBaseUrl() + "passdata";
    }

    @Test
    public void restPassDataCheckPassword() {
        String url = getBaseUrl() + "/password";

        JsonMapper mapper = new JsonMapper();
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
        assertThat(context.getInitParameter(PassDataFileManagementService.PASS_DATA_FILE_NAME_PARAM_NAME)).isEqualTo("data/test1.vnf");
        String fileNameResult = this.restTemplate.getForObject(getBaseUrl() + "/filename", String.class);
        assertThat(fileNameResult).contains("file exists:true");
    }

    @Test
    public void passData() {
        String url = getBaseUrl();

        JsonMapper mapper = new JsonMapper();
        ObjectNode passwordNode = mapper.createObjectNode();
        passwordNode.put("password", "123456");

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(passwordNode.toString(), headers);
        PassDataDTO answer = restTemplate.postForObject(url, entity, PassDataDTO.class);

        assertThat(answer.passCategoryList.size()).isEqualTo(4);
        assertThat(answer.passNoteList.size()).isEqualTo(7);
    }

    @Test
    @Disabled
    public void passDataDropbox() {
        String url = getBaseUrl() + "dropbox";

        JsonMapper mapper = new JsonMapper();
        ObjectNode passwordNode = mapper.createObjectNode();
        passwordNode.put("password", "123456");
        passwordNode.put("auth-key", "IC-VZ7nhRZAAAAAAAAAAZTIxapCGU5yAnXt0o4PF1vjycHkNTXfOmrqQktgQil5Y");

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(passwordNode.toString(), headers);
        PassDataDTO answer = restTemplate.postForObject(url, entity, PassDataDTO.class);

        assertThat(answer.passCategoryList.size()).isEqualTo(4);
        assertThat(answer.passNoteList.size()).isEqualTo(7);
    }


    @Test
    public void passDataWrongPassword() {
        String url = getBaseUrl();

        JsonMapper mapper = new JsonMapper();
        ObjectNode passwordNode = mapper.createObjectNode();
        passwordNode.put("password", "1234567");

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(passwordNode.toString(), headers);

        ErrorResponse answer = restTemplate.postForObject(url, entity, ErrorResponse.class);
        assertThat(answer.errorCode).isEqualTo(404);

        String stringAnswer = restTemplate.postForObject(url, entity, String.class);
        assertThat(stringAnswer).contains("errorMessage");
        assertThat(stringAnswer).contains("errorCode");
    }
}
