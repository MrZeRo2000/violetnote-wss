package com.romanpulov.violetnotewss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassNote2DTO {
    public final String system;
    public final String user;
    public final String password;
    public final String url;
    public final String info;
    public final Date createdDate;
    public final Date modifiedDate;
    public final Boolean active;

    @JsonCreator
    public PassNote2DTO(
            @JsonProperty("system")
                    String system,
            @JsonProperty("user")
                    String user,
            @JsonProperty("password")
                    String password,
            @JsonProperty("url")
                    String url,
            @JsonProperty("info")
                    String info,
            @JsonProperty("createdDate")
                    Date createdDate,
            @JsonProperty("modifiedDate")
                    Date modifiedDate,
            @JsonProperty("active")
                    Boolean active
    ) {
        this.system = system;
        this.user = user;
        this.password = password;
        this.url = url;
        this.info = info;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.active = active;
    }

    @Override
    public String toString() {
        return "PassNote2DTO{" +
                "system='" + system + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", info='" + info + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", active=" + active +
                '}';
    }
}
