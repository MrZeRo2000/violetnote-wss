package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.romanpulov.violetnotecore.Model.PassData;

public class PassDataPersistRequest extends PassDataGetRequest {
    @JsonProperty("passData")
    private final PassData passData;

    @JsonCreator
    public PassDataPersistRequest(
            @JsonProperty("fileName")
            String fileName,
            @JsonProperty("password")
            String password,
            @JsonProperty("passData")
            PassData passData
    ) {
        super(fileName, password);
        this.passData = passData;
    }

    public PassData getPassData() {
        return passData;
    }

    @Override
    public String toString() {
        return "PassDataOperationRequest{" +
                "passData='" + passData + '\'' +
                ", fileName='" + getFileName() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
