package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Basic tests
 */
public class SimpleTest {

    @Test
    public void objectMapperTest() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode passwordNode = mapper.createObjectNode();
        passwordNode.put("password", "pass123");
        assertThat(passwordNode.toString()).isEqualTo("{\"password\":\"pass123\"}");
    }
}
