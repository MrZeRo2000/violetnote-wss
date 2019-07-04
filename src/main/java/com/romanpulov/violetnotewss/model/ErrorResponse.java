package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    @JsonProperty("errorCode")
    public final int errorCode;

    @JsonProperty("errorMessage")
    public final String errorMessage;

    @JsonCreator
    public ErrorResponse(@JsonProperty("errorCode") int errorCode, @JsonProperty("errorMessage") String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
