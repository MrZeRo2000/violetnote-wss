package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PassNote2DTO(String system, String user, String password, String url, String info, Date createdDate,
                           Date modifiedDate, Boolean active, List<PassDataAttributeDTO> attributes) {}
