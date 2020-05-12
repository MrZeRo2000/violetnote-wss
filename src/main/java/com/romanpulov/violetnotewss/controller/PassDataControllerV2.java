package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/passdata")
public class PassDataControllerV2 {

    private final PassDataManagementService passDataManagementService;

    public PassDataControllerV2(PassDataManagementService passDataManagementService) {
        this.passDataManagementService = passDataManagementService;
    }

    @PostMapping("")
    ResponseEntity<PassData> getPassData(@RequestBody PassDataGetRequest getRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException
    {
        return ResponseEntity.ok(passDataManagementService.readPassData(getRequest, getRequest.getFileName()));
    }
}
