package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassDataAuthInfo extends PassDataInfo {
    @JsonProperty("auth-key")
    public final String authKey;

    @JsonCreator
    public PassDataAuthInfo (@JsonProperty("password") String password, @JsonProperty("auth-key") String authKey) {
        super(password);
        this.authKey = authKey;
    }
}
