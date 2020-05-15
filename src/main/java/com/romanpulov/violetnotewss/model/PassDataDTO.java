package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PassDataDTO {

    @JsonProperty("passCategoryList")
    public final List<PassCategoryDTO> passCategoryList;

    @JsonProperty("passNoteList")
    public final List<PassNoteDTO> passNoteList;

    @JsonCreator
    public PassDataDTO(
            @JsonProperty("passCategoryList")
            List<PassCategoryDTO> passCategoryList,
            @JsonProperty("passNoteList")
            List<PassNoteDTO> passNoteList) {
        this.passCategoryList = passCategoryList;
        this.passNoteList = passNoteList;
    }

    @Override
    public String toString() {
        return "PassDataDTO{" +
                "passCategoryList=" + passCategoryList +
                ", passNoteList=" + passNoteList +
                '}';
    }
}
