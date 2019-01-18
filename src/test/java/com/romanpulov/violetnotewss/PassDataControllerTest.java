package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for PassDataController
 */
public class PassDataControllerTest extends BaseApplicationTest {

    @Override
    protected String getBaseUrl() {
        return super.getBaseUrl() + "passdata/";
    }

    @Test
    public void restPassDataCheckPassword() {
        String url = getBaseUrl() + "checkpasswordpost";

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
        String fileNameResult = this.restTemplate.getForObject(getBaseUrl() + "filename", String.class);
        assertThat(fileNameResult).contains("file exists:true");
    }
}
