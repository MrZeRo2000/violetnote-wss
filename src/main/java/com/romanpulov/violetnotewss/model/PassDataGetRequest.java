package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PassData request class for v2 API
 */
public class PassDataGetRequest extends PassDataFileRequest implements PasswordProvider {

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
        super(fileName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PassDataGetRequest{" +
                "fileName='" + getFileName() + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
