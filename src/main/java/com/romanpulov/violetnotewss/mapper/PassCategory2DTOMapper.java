package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotewss.model.PassCategory2DTO;

import java.util.stream.Collectors;

public class PassCategory2DTOMapper {
    public static PassCategory2 dtoToCore(PassCategory2DTO dto) {
        return PassCategory2.createWithNotes(
                dto.categoryName,
                dto.passNote2List.stream().map(PassNote2DTOMapper::dtoToCore).collect(Collectors.toList())
        );
    }

    public static PassCategory2DTO coreToDTO(PassCategory2 core) {
        return new PassCategory2DTO(
                core.getCategoryName(),
                core.getNoteList().stream().map(PassNote2DTOMapper::coreToDTO).collect(Collectors.toList())
        );
    }
}
