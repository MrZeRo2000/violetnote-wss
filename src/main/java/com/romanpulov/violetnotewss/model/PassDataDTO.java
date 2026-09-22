package com.romanpulov.violetnotewss.model;

import java.util.List;

public record PassDataDTO(List<PassCategoryDTO> passCategoryList, List<PassNoteDTO> passNoteList) {}
