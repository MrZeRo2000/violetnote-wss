package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication info for PassData
 */
public class PassDataInfo {
    @JsonProperty("password")
    public final String password;

    public boolean isEmptyPassword() {
        return password == null || password.isEmpty();
    }

    @JsonCreator
    public PassDataInfo (@JsonProperty("password") String password) {
        this.password = password;
    }
}
