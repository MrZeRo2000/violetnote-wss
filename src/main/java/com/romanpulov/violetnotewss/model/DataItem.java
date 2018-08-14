package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataItem {
    private int id;

    private String name;

    public void setId(int value) {
        id = value;
    }

    @JsonProperty("my property id")
    public int getId() {
        return id;
    }

    public void setName(String value) {
        name = value;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

}
