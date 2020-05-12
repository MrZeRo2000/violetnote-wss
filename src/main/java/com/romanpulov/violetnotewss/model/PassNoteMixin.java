package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface PassNoteMixin {
    @JsonIgnore()
    Map<String, String> getNoteAttr();
}
