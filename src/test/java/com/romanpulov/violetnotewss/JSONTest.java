package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.romanpulov.violetnotewss.model.PassCategoryDTO;
import com.romanpulov.violetnotewss.model.PassDataDTO;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        PassCategoryDTO passCategory = new PassCategoryDTO("Category 1", null);
        String json = mapper.writeValueAsString(passCategory);
        System.out.println("Json for category:" + json);
    }

    @Test
    public void dsCategory() throws Exception {
        String categoryString = "{\"categoryName\":\"Category 1\",\"parentCategory\":null}";
        ObjectMapper mapper = new ObjectMapper();

        PassCategoryDTO category = mapper.readValue(categoryString, PassCategoryDTO.class);
        assertThat(category.categoryName).isEqualTo("Category 1");
    }

    @Test
    public void dsPassData() throws Exception {
        byte[] encodedJson = Files.readAllBytes(Paths.get("data/test1.json"));
        String json = new String(encodedJson, Charset.defaultCharset());

        ObjectMapper mapper = new ObjectMapper();
        PassDataDTO passData = mapper.readValue(json, PassDataDTO.class);
        assertThat(passData.passCategoryList.size()).isEqualTo(4);
        assertThat(passData.passNoteList.size()).isEqualTo(7);
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

    @JsonIgnoreProperties(value = { "attrs", "y" })
    static class TestMissedClass {
        public int x;
    }

    @Test
    public void missedClassMembers() throws Exception {
        String json = "{\"x\":7,\"attrs\":{\"Name1\":\"Value1\"},\"y\":43}";
        ObjectMapper objectMapper = new ObjectMapper();
        TestMissedClass tc = objectMapper.readValue(json, TestMissedClass.class);

        assertThat(7).isEqualTo(tc.x);
    }

    @JsonIgnoreProperties(value = { "attrs", "x" })
    static class TestParseDates {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @JsonProperty("server_modified")
        public Date modifiedDate;
    }

    @Test
    public void parseDates() throws Exception {
        String json = "{\"x\":7,\"attrs\":{\"Name1\":\"Value1\"},\"server_modified\":\"2016-06-30T14:58:44Z\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TestParseDates tc = objectMapper.readValue(json, TestParseDates.class);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = formatter.parse("2016-06-30T14:58:44Z");

        assertThat(date).isEqualTo(tc.modifiedDate);
    }

}
