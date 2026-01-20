package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassNoteDTO {
    public final PassCategoryDTO passCategory;
    public final String system;
    public final String user;
    public final String password;
    public final String comments;
    public final String custom;
    public final String info;
    public final Date createdDate;
    public final Date modifiedDate;
    public final Boolean active;

    @JsonIgnore()
    public final Map<String, String> noteAttr = null;

    @JsonCreator
    public PassNoteDTO(
            @JsonProperty("passCategory")
            PassCategoryDTO passCategory,
            @JsonProperty("system")
            String system,
            @JsonProperty("user")
            String user,
            @JsonProperty("password")
            String password,
            @JsonProperty("comments")
            String comments,
            @JsonProperty("custom")
            String custom,
            @JsonProperty("info")
            String info,
            @JsonProperty("createdDate")
            Date createdDate,
            @JsonProperty("modifiedDate")
            Date modifiedDate,
            @JsonProperty("active")
            Boolean active
    ) {
        this.passCategory = passCategory;
        this.system = system;
        this.user = user;
        this.password = password;
        this.comments = comments;
        this.custom = custom;
        this.info = info;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.active = active;
    }

    @Override
    public String toString() {
        return "PassNoteDTO{" +
                "passCategory=" + passCategory +
                ", system='" + system + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", comments='" + comments + '\'' +
                ", custom='" + custom + '\'' +
                ", info='" + info + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", active=" + active +
                '}';
    }
}
