package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassData2PersistRequest extends PassDataGetRequest {
    @JsonProperty("passData")
    private final PassData2DTO passData2;

    @JsonCreator
    public PassData2PersistRequest(
            @JsonProperty("fileName")
                    String fileName,
            @JsonProperty("password")
                    String password,
            @JsonProperty("passData")
                    PassData2DTO passData2
    ) {
        super(fileName, password);
        this.passData2 = passData2;
    }

    public PassData2DTO getPassData() {
        return passData2;
    }

    @Override
    public String toString() {
        return "PassData2PersistRequest{" +
                "passData='" + passData2 + '\'' +
                ", fileName='" + getFileName() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
