package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassDataOperationRequest extends PassDataGetRequest {
    @JsonProperty("operation")
    public final String operation;

    @JsonCreator
    public PassDataOperationRequest(
            @JsonProperty("operation")
            String operation,
            @JsonProperty("fileName")
            String fileName,
            @JsonProperty("password")
            String password
    ) {
        super(fileName, password);
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "PassDataOperationRequest{" +
                "operation='" + operation + '\'' +
                ", fileName='" + fileName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
