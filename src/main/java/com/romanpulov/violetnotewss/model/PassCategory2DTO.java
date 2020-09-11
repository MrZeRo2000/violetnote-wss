package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PassCategory2DTO {
    @JsonProperty("categoryName")
    public final String categoryName;

    @JsonProperty("parentCategory")
    public final PassCategoryDTO parentCategory;

    @JsonCreator
    public PassCategory2DTO(
            @JsonProperty("categoryName")
                    String categoryName,
            @JsonProperty("parentCategory")
                    PassCategoryDTO parentCategory
    ) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
    }

    @Override
    public String toString() {
        return "PassCategory2DTO{" +
                "categoryName='" + categoryName + '\'' +
                ", parentCategory=" + parentCategory +
                '}';
    }
}
