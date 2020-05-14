package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotewss.model.PassNoteDTO;
import org.springframework.stereotype.Component;

@Component
public class PassNoteDTOMapper {

    private final PassCategoryDTOMapper passCategoryDTOMapper;

    public PassNoteDTOMapper(PassCategoryDTOMapper passCategoryDTOMapper) {
        this.passCategoryDTOMapper = passCategoryDTOMapper;
    }

    public PassNote dtoToCore(PassNoteDTO dto) {
        return new PassNote(
            passCategoryDTOMapper.dtoToCore(dto.passCategory),
                dto.system,
                dto.user,
                dto.password,
                dto.comments,
                dto.custom,
                dto.info
        );
    }
}
