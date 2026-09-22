package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassDataAuthInfo extends PassDataInfo {
    @JsonProperty("auth-name")
    public final String authKey;

    @JsonCreator
    public PassDataAuthInfo (@JsonProperty("password") String password, @JsonProperty("auth-name") String authKey) {
        super(password);
        this.authKey = authKey;
    }
}
