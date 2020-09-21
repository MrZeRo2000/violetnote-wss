package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotewss.model.PassNote2DTO;
import org.springframework.stereotype.Component;

public class PassNote2DTOMapper {
    public static PassNote2 dtoToCore(PassNote2DTO dto) {
        return new PassNote2(
                dto.system,
                dto.user,
                dto.password,
                dto.url,
                dto.info,
                dto.createdDate,
                dto.modifiedDate
        );
    }

    public static PassNote2DTO coreToDTO(PassNote2 core) {
        return new PassNote2DTO(
                core.getSystem(),
                core.getUser(),
                core.getPassword(),
                core.getUrl(),
                core.getInfo(),
                core.getCreatedDate(),
                core.getModifiedDate(),
                true
        );
    }
}
