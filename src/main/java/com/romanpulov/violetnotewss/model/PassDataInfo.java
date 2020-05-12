package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication info for PassData
 */
public class PassDataInfo implements PasswordProvider {
    @JsonProperty("password")
    public final String password;

    public boolean isPasswordEmpty() {
        return password == null || password.isEmpty();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @JsonCreator
    public PassDataInfo (@JsonProperty("password") String password) {
        this.password = password;
    }
}
