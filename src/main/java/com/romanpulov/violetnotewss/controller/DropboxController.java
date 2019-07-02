package com.romanpulov.violetnotewss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.romanpulov.violetnotewss.model.AuthCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dropbox")
public class DropboxController {

    @RequestMapping(path = "/auth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthCode setAuthCode(@RequestParam(name = "code") String authCode) {
        return new AuthCode(authCode);
    }
}
