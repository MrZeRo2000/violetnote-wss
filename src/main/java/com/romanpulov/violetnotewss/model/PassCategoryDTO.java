package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassCategoryDTO {
    @JsonProperty("categoryName")
    public final String categoryName;

    @JsonProperty("parentCategory")
    public final PassCategoryDTO parentCategory;

    @JsonCreator
    public PassCategoryDTO(
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
        return "PassCategoryDTO{" +
                "categoryName='" + categoryName + '\'' +
                ", parentCategory=" + parentCategory +
                '}';
    }
}
