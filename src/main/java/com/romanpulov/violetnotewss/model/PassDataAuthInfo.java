package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication info for PassData
 */
public class PassDataAuthInfo {
    @JsonProperty("password")
    public String password;
}
