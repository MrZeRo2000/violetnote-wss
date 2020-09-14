package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PassData2DTO {
    @JsonProperty("passCategoryList")
    public final List<PassCategory2DTO> passCategoryList;

    @JsonCreator
    public PassData2DTO(@JsonProperty("passCategoryList") List<PassCategory2DTO> passCategoryList) {
        this.passCategoryList = passCategoryList;
    }

    @Override
    public String toString() {
        return "PassData2DTO{" +
                "passCategoryList=" + passCategoryList +
                '}';
    }
}
