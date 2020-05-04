package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotewss.model.AuthCode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class DropboxControllerTest extends BaseApplicationTest  {

    @Override
    protected String getBaseUrl() {
        return super.getBaseUrl() + "dropbox/";
    }

    @Test
    public void authCode() throws Exception {
        String testCode = "r436";

        ResponseEntity<String> response = this.restTemplate.getForEntity(getBaseUrl() + "auth?code=" + testCode, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ObjectMapper mapper = new ObjectMapper();

        AuthCode code = mapper.readValue(response.getBody(), AuthCode.class);
        assertThat(code.getCode()).isEqualTo(testCode);
    }

}
