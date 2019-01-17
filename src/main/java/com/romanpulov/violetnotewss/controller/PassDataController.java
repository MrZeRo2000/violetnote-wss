package com.romanpulov.violetnotewss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.model.ErrorResponse;
import com.romanpulov.violetnotewss.model.PassDataInfo;
import com.romanpulov.violetnotewss.services.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.services.PassDataFileReadException;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;

@RestController("passdata")
public class PassDataController {

    private PassDataManagementService passDataManagementService;

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
            path = "checkpassword" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT
    )
    public String checkPassword(@RequestBody PassDataInfo passDataInfo) {
        return passDataInfo.password;
    }

    @RequestMapping(
            path = "checkpassword1" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_HTML_VALUE,
            method = RequestMethod.PUT
    )
    public String checkPassword1(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            PassDataInfo passDataInfo = mapper.readValue(json, PassDataInfo.class);
            return passDataInfo.password;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(
            path = "checkpasswordpost" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public String checkPasswordPost(@RequestBody PassDataInfo passDataInfo) {
        return passDataInfo.password;
    }

    @RequestMapping(
            path = "checkpasswordpostex" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public String checkPasswordPostEx(@RequestBody PassDataInfo passDataInfo) throws PassDataFileNotFoundException {
        throw new PassDataFileNotFoundException("file not found");
    }

    @RequestMapping(
            path = "readdatapost" ,
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
