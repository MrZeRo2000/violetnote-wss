package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PassData request class for v2 API
 */
public class PassDataGetRequest {
    @JsonProperty("fileName")
    public final String fileName;

    @JsonProperty("password")
    public final String password;

    @JsonCreator
    public PassDataGetRequest(
            @JsonProperty("fileName")
            String fileName,
            @JsonProperty("password")
            String password) {
        this.fileName = fileName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "PassDataGetRequest{" +
                "fileName='" + fileName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
