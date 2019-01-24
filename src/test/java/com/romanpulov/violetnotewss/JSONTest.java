package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Basic tests
 */
public class JSONTest {

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

    static abstract class TestMixin {
        @JsonProperty("a")
        int x;
        @JsonIgnore()
        abstract Map<String, String> getAttrs();
    }

    static class TestClass {
        public int x;
        public Map<String, String> getAttrs() {
            Map<String, String> result = new HashMap<>();
            result.put("Name1", "Value1");
            return result;
        }
    }

    @Test
    public void mixinTest() throws Exception {

        TestClass tc = new TestClass();
        tc.x = 7;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tc);
        assertThat(json).isEqualTo("{\"x\":7,\"attrs\":{\"Name1\":\"Value1\"}}");

        ObjectMapper mixinObjectMapper = new ObjectMapper();
        mixinObjectMapper.addMixIn(TestClass.class, TestMixin.class);
        String jsonMixin = mixinObjectMapper.writeValueAsString(tc);

        assertThat(jsonMixin).isEqualTo("{\"a\":7}");
    }

}
