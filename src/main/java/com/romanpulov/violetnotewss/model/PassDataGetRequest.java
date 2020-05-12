package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PassData request class for v2 API
 */
public class PassDataGetRequest implements PasswordProvider {
    @JsonProperty("fileName")
    private final String fileName;

    @JsonProperty("password")
    private final String password;

    @JsonIgnore
    public boolean isPasswordEmpty() {
        return password == null || password.isEmpty();
    }

    @JsonCreator
    public PassDataGetRequest(
            @JsonProperty("fileName")
            String fileName,
            @JsonProperty("password")
            String password) {
        this.fileName = fileName;
        this.password = password;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PassDataGetRequest{" +
                "fileName='" + fileName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
