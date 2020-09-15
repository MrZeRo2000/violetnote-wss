package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotewss.model.PassData2DTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PassData2DTOMapper {
    private final PassCategory2DTOMapper passCategory2DTOMapper;

    public PassData2DTOMapper(PassCategory2DTOMapper passCategory2DTOMapper) {
        this.passCategory2DTOMapper = passCategory2DTOMapper;
    }

    public PassData2 dtoToCore(PassData2DTO dto) {
        return new PassData2(
                dto.passCategoryList.stream().map(passCategory2DTOMapper::dtoToCore).collect(Collectors.toList())
        );
    }
}
