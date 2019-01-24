package com.romanpulov.violetnotewss.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotewss.model.ErrorResponse;
import com.romanpulov.violetnotewss.model.PassDataInfo;
import com.romanpulov.violetnotewss.services.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.services.PassDataFileReadException;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/passdata")
public class PassDataController {

    interface PassNoteMixin {
        @JsonIgnore()
        Map<String, String> getNoteAttr();
    }

    @Configuration
    public class JacksonConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.addMixIn(PassNote.class, PassNoteMixin.class);
            return mapper;
        }
    }

    private final PassDataManagementService passDataManagementService;

    public PassDataController(@Autowired PassDataManagementService passDataManagementService) {
        this.passDataManagementService = passDataManagementService;
    }

    @RequestMapping("/filename")
    public String getFileName() {
        String fileName = passDataManagementService.getPassDataFileName();
        File f = new File(fileName);
        return "FileName:" + fileName + ", file exists:" + f.exists();
    }

    @RequestMapping(
            path = "/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public String checkPasswordPost(@RequestBody PassDataInfo passDataInfo) {
        return passDataInfo.password;
    }

    @RequestMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public PassData readDataPost(@RequestBody PassDataInfo passDataInfo)
        throws PassDataFileNotFoundException, PassDataFileReadException
    {
        return passDataManagementService.readPassData(passDataInfo);
    }

    @ExceptionHandler({PassDataFileNotFoundException.class, PassDataFileReadException.class})
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
