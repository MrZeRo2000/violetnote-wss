package com.romanpulov.violetnotewss.mapper;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotewss.model.PassCategory2DTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PassCategory2DTOMapper {

    private final PassNote2DTOMapper passNote2DTOMapper;

    public PassCategory2DTOMapper(PassNote2DTOMapper passNote2DTOMapper) {
        this.passNote2DTOMapper = passNote2DTOMapper;
    }

    public PassCategory2 dtoToCore(PassCategory2DTO dto) {
        return PassCategory2.createWithNotes(
                dto.categoryName,
                dto.passNote2List.stream().map(passNote2DTOMapper::dtoToCore).collect(Collectors.toList())
        );
    }
}
