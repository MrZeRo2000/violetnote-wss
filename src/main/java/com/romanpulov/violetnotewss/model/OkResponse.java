package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OkResponse {
    @JsonProperty("message")
    public final String message;

    @JsonCreator
    public OkResponse(@JsonProperty("message") String message) {
        this.message = message;
    }
}
