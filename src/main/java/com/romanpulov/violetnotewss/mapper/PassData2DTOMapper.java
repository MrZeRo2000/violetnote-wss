package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotewss.model.PassData2DTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

public class PassData2DTOMapper {
    public static PassData2 dtoToCore(PassData2DTO dto) {
        return new PassData2(
                dto.passCategoryList.stream().map(PassCategory2DTOMapper::dtoToCore).collect(Collectors.toList())
        );
    }

    public static PassData2DTO coreToDTO(PassData2 core) {
        return new PassData2DTO(
                core.getCategoryList().stream().map(PassCategory2DTOMapper::coreToDTO).collect(Collectors.toList())
        );
    }
}
