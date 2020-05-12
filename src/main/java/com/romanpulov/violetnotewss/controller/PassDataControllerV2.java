package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/passdata")
public class PassDataControllerV2 {

    @PostMapping("")
    ResponseEntity<PassData> getPassData(@RequestBody PassDataGetRequest getRequest) {
        return ResponseEntity.ok(new PassData());
    }
}
