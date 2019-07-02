package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthCode {
    public String getCode() {
        return code;
    }

    private final String code;

    @JsonCreator
    public AuthCode(@JsonProperty("code") String code) {
        this.code = code;
    }
}
