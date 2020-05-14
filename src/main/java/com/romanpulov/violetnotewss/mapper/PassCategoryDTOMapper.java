package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotewss.model.PassCategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class PassCategoryDTOMapper {
    public PassCategory dtoToCore(PassCategoryDTO dto) {
        return new PassCategory(
                dto.categoryName,
                dto.parentCategory == null ? null : dtoToCore(dto.parentCategory)
        );
    }
}
