package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassDataAttribute;
import com.romanpulov.violetnotewss.model.PassDataAttributeDTO;

public class PassDataAttributeDTOMapper {
    public static PassDataAttribute dtoToCore(PassDataAttributeDTO dto) {
        return new PassDataAttribute(dto.name(), dto.value());
    }

    public static PassDataAttributeDTO coreToDto(PassDataAttribute core) {
        return new PassDataAttributeDTO(core.name(), core.value());
    }
}
