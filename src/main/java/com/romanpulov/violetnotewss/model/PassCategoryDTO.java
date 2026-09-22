package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PassCategoryDTO(String categoryName, PassCategoryDTO parentCategory) { }
