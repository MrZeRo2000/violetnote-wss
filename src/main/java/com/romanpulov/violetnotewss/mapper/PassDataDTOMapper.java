package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.model.PassDataDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PassDataDTOMapper {
    private final PassCategoryDTOMapper passCategoryDTOMapper;

    private final PassNoteDTOMapper passNoteDTOMapper;

    public PassDataDTOMapper(PassCategoryDTOMapper passCategoryDTOMapper, PassNoteDTOMapper passNoteDTOMapper) {
        this.passCategoryDTOMapper = passCategoryDTOMapper;
        this.passNoteDTOMapper = passNoteDTOMapper;
    }

    public PassData dtoToCore(PassDataDTO passDataDTO) {
        return new PassData(
                passDataDTO.passCategoryList.stream().map(passCategoryDTOMapper::dtoToCore).collect(Collectors.toList()),
                passDataDTO.passNoteList.stream().map(passNoteDTOMapper::dtoToCore).collect(Collectors.toList())
        );
    }
}
