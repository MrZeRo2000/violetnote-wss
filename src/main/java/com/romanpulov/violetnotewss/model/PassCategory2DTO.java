package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PassCategory2DTO {
    @JsonProperty("categoryName")
    public final String categoryName;

    @JsonProperty("passNoteList")
    public final List<PassNote2DTO> passNote2List;

    @JsonCreator
    public PassCategory2DTO(
            @JsonProperty("categoryName")
                    String categoryName,
            @JsonProperty("passNoteList")
                    List<PassNote2DTO> passNote2List
    ) {
        this.categoryName = categoryName;
        this.passNote2List = passNote2List;
    }

    @Override
    public String toString() {
        return "PassCategory2DTO{" +
                "categoryName='" + categoryName + '\'' +
                ", passNote2List=" + passNote2List +
                '}';
    }
}
