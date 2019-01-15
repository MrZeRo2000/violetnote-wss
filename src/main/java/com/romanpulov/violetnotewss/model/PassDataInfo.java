package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication info for PassData
 */
public class PassDataInfo {
    @JsonProperty("password")
    public String password;
}