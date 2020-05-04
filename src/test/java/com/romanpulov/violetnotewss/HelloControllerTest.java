package com.romanpulov.violetnotewss;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for HelloController
 */
public class HelloControllerTest extends BaseApplicationTest {

    @Override
    protected String getBaseUrl() {
        return super.getBaseUrl() + "hello/";
    }

    @Test
    public void baseUrl() {
        assertThat(this.restTemplate.getForObject(getBaseUrl(), String.class)).contains("Hello from HelloController");
    }

    @Test
    public void dataItem() {
        assertThat(this.restTemplate.getForObject(getBaseUrl() + "dataitem", String.class)).isEqualTo("{\"my property id\":10}");
    }
}
