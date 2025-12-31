package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassDataPersistRequest extends PassDataGetRequest {
    @JsonProperty("passData")
    private final PassDataDTO passData;

    @JsonCreator
    public PassDataPersistRequest(
            @JsonProperty("fileName")
            String fileName,
            @JsonProperty("password")
            String password,
            @JsonProperty("passData")
            PassDataDTO passData
    ) {
        super(fileName, password);
        this.passData = passData;
    }

    public PassDataDTO getPassData() {
        return passData;
    }

    @Override
    public String toString() {
        return "PassDataPersistRequest{" +
                "passData='" + passData + '\'' +
                ", fileName='" + getFileName() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
