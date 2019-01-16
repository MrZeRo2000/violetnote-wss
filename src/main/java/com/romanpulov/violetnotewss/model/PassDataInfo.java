package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication info for PassData
 */
public class PassDataInfo {
    @JsonProperty("password")
    public String password;

    public boolean isEmptyPassword() {
        return password == null || password.isEmpty();
    }

    public static PassDataInfo fromString(String password) {
        PassDataInfo instance = new PassDataInfo();
        instance.password = password;

        return instance;
    }
}
