package com.romanpulov.violetnotewss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotewss.model.ErrorResponse;
import com.romanpulov.violetnotewss.model.PassDataAuthInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController("passdata")
public class PassDataController {

    @RequestMapping(
            path = "checkpassword" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT
    )
    public String checkPassword(@RequestBody PassDataAuthInfo passDataAuthInfo) {
        return passDataAuthInfo.password;
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
            PassDataAuthInfo passDataAuthInfo = mapper.readValue(json, PassDataAuthInfo.class);
            return passDataAuthInfo.password;
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
    public String checkPasswordPost(@RequestBody PassDataAuthInfo passDataAuthInfo) {
        return passDataAuthInfo.password;
    }

    @RequestMapping(
            path = "checkpasswordpostex" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public String checkPasswordPostEx(@RequestBody PassDataAuthInfo passDataAuthInfo) throws FileNotFoundException {
        throw new FileNotFoundException("file not found");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(FileNotFoundException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
