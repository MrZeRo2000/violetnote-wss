package com.romanpulov.violetnotewss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotewss.model.PassDataAuthInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("passdata")
public class PassDataController {

    @RequestMapping(path = "checkpassword" ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.PUT)
    public String checkPassword(@RequestBody PassDataAuthInfo passDataAuthInfo) {
        return passDataAuthInfo.password;
    }

    @RequestMapping(path = "checkpassword1" ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.PUT)
    public String checkPassword1(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            PassDataAuthInfo passDataAuthInfo = mapper.readValue(json, PassDataAuthInfo.class);
            return passDataAuthInfo.password;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
