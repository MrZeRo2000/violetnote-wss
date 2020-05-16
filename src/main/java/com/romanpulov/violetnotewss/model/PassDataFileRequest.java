package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassDataFileRequest {
    @JsonProperty("fileName")
    private final String fileName;

    @JsonCreator
    public PassDataFileRequest(@JsonProperty("fileName") String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "PassDataFileRequest{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
