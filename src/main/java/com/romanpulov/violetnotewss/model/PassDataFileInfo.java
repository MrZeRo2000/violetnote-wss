package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PassData File information
 */
public class PassDataFileInfo {
    @JsonProperty("name")
    public final String fileName;

    @JsonProperty("exists")
    public final boolean fileExists;

    @JsonCreator
    public PassDataFileInfo(
            @JsonProperty("name") String fileName,
            @JsonProperty("exists") boolean fileExists
    ) {
        this.fileName = fileName;
        this.fileExists = fileExists;
    }
}
