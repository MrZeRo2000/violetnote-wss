package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

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

    @Test
    public void serializeCategory() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PassData.PassCategory passCategory = new PassData.PassCategory();
        passCategory.categoryName = "Category 1";
        String json = mapper.writeValueAsString(passCategory);
        System.out.println("Json for category:" + json);
    }

    @Test
    public void dsCategory() throws Exception {
        String categoryString = "{\"categoryName\":\"Category 1\",\"parentCategory\":null}";
        ObjectMapper mapper = new ObjectMapper();

        PassData.PassCategory category = mapper.readValue(categoryString, PassData.PassCategory.class);
        assertThat(category.categoryName).isEqualTo("Category 1");
    }

    @Test
    public void dsPassData() throws Exception {
        byte[] encodedJson = Files.readAllBytes(Paths.get("data/test1.json"));
        String json = new String(encodedJson, Charset.defaultCharset());

        ObjectMapper mapper = new ObjectMapper();
        PassData passData = mapper.readValue(json, PassData.class);
        assertThat(passData.passCategoryList.length).isEqualTo(4);
        assertThat(passData.passNoteList.length).isEqualTo(7);
    }

}
